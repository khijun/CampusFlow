// iframe 컨테이너를 저장할 객체
const iframeCache = new Map();

// 최대 탭 개수 설정
const MAX_TABS = 7;

// 드래그 관련 변수 추가
let dragTab = null;

/**
 * 현재 열려있는 탭들의 상태를 sessionStorage에 저장하는 함수
 * 각 탭의 제목, URL, 활성화 상태를 저장
 */
function saveTabState() {
    const tabs = document.getElementById('tabs');
    const tabsData = Array.from(tabs.children).map(tab => ({
        title: tab.textContent.trim().replace('×', ''), // 닫기 버튼 텍스트 제거
        url: tab.getAttribute('data-url'),
        active: tab.classList.contains('active')
    }));
    sessionStorage.setItem('tabState', JSON.stringify(tabsData));
}

/**
 * sessionStorage에서 저장된 탭 상태를 복원하는 함수
 * 페이지 로드 시 이전 탭 상태를 복원
 */
function restoreTabState() {
    const savedState = sessionStorage.getItem('tabState');
    if (savedState) {
        const tabsData = JSON.parse(savedState);
        const tabs = document.getElementById('tabs');
        tabs.innerHTML = ''; // 기존 탭 초기화

        tabsData.forEach(tabData => {
            addTab(tabData.title, tabData.url, false); // 저장된 탭 복원
            if (tabData.active) {
                switchTab(tabData.url);
            }
        });
    }
}

/**
 * iframe 내용을 캐시하고 로드하는 함수
 * @param {string} url - 로드할 URL
 */
function loadIframe(url) {
    const container = document.querySelector('.iframe-container');

    // 처음 로드하는 URL이면 새 iframe 생성
    if (!iframeCache.has(url)) {
        const newIframe = document.createElement('iframe');
        newIframe.name = 'contentFrame';
        newIframe.src = url;
        iframeCache.set(url, newIframe);
        container.appendChild(newIframe);
    }

    // 모든 iframe 숨기기
    iframeCache.forEach(frame => {
        frame.style.display = 'none';
    });

    // 현재 URL의 iframe 표시
    const currentFrame = iframeCache.get(url);
    if (currentFrame) {
        currentFrame.style.display = 'block';
    }
}

/**
 * 새로운 탭을 추가하는 함수
 * @param {string} title - 탭 제목
 * @param {string} url - 탭 내용을 표시할 URL
 * @param {boolean} saveState - 탭 상태 저장 여부 (기본값: true)
 */
function addTab(title, url, saveState = true) {
    const tabs = document.getElementById('tabs');

    // 이미 존재하는 탭인지 확인
    const existingTab = Array.from(tabs.children).find(tab => tab.getAttribute('data-url') === url);
    if (existingTab) {
        switchTab(url);
        return;
    }

    // 최대 탭 개수 체크
    if (tabs.children.length >= MAX_TABS) {
        alert('최대 ' + MAX_TABS + '개의 탭만 열 수 있습니다.');
        return;
    }

    // 새로운 탭 요소 생성
    const newTab = document.createElement('li');
    newTab.className = 'tab-item';
    newTab.setAttribute('data-url', url);
    newTab.innerHTML = `
        <span>${title}</span>
        <button class="close-tab" onclick="closeTab(this)">×</button>
    `;
    newTab.onclick = () => switchTab(url);

    // 드래그 앤 드롭 기능 추가
    newTab.draggable = true;  // 드래그 가능하도록 설정
    newTab.addEventListener('dragstart', handleDragStart);  // 드래그 시작 이벤트
    newTab.addEventListener('dragover', handleDragOver);    // 드래그 중 이벤트
    newTab.addEventListener('drop', handleDrop);           // 드롭 이벤트
    newTab.addEventListener('dragend', handleDragEnd);     // 드래그 종료 이벤트

    // 탭 추가 및 활성화
    tabs.appendChild(newTab);
    switchTab(url);

    // 상태 저장 옵션이 true인 경우에만 저장
    if (saveState) {
        saveTabState();
    }
}

/**
 * 드래그 시작 시 호출되는 함수
 * @param {DragEvent} e - 드래그 이벤트 객체
 */
function handleDragStart(e) {
    // 닫기 버튼 클릭 시 드래그 방지
    if (e.target.classList.contains('close-tab')) {
        e.preventDefault();
        return;
    }
    dragTab = e.target.closest('.tab-item');
    dragTab.style.opacity = '0.4';

    // 드래그 이미지 설정
    const dragImage = dragTab.cloneNode(true);
    dragImage.style.opacity = '0'; // 기본 드래그 이미지를 투명하게
    document.body.appendChild(dragImage);
    e.dataTransfer.setDragImage(dragImage, 0, 0);

    // 다음 프레임에서 임시 요소 제거
    requestAnimationFrame(() => {
        document.body.removeChild(dragImage);
    });

    e.dataTransfer.effectAllowed = 'move';
}

/**
 * 드래그 오버 시 호출되는 함수
 * @param {DragEvent} e - 드래그 이벤트 객체
 */
function handleDragOver(e) {
    if (!dragTab) return;
    e.preventDefault();
    e.dataTransfer.dropEffect = 'move';

    const tab = e.target.closest('.tab-item');
    if (tab && tab !== dragTab) {
        const rect = tab.getBoundingClientRect();
         const midPoint = rect.x + (rect.width / 2); // 50% 지점으로 수정

        // 기존 모든 탭의 경계선 스타일 초기화
        const tabs = document.getElementById('tabs');
        Array.from(tabs.children).forEach(t => {
            t.style.borderLeft = '';
            t.style.borderRight = '';
        });

        // 현재 탭에만 경계선 표시
        if (e.clientX < midPoint) {
            tab.style.borderLeft = '2px solid #000';
        } else {
            tab.style.borderRight = '2px solid #000';
        }
    }
}

/**
 * 드롭 시 호출되는 함수
 * @param {DragEvent} e - 드래그 이벤트 객체
 */
function handleDrop(e) {
    e.preventDefault();  // 기본 동작 방지

    const tab = e.target.closest('.tab-item');  // 드롭된 위치의 탭
    if (tab && dragTab !== tab) {
        // 드롭 위치에 따라 탭 순서 변경
        const rect = tab.getBoundingClientRect();
        const midPoint = rect.x + rect.width / 2;

        if (e.clientX < midPoint) {
            tab.parentNode.insertBefore(dragTab, tab);  // 왼쪽에 삽입
        } else {
            tab.parentNode.insertBefore(dragTab, tab.nextSibling);  // 오른쪽에 삽입
        }

        saveTabState();  // 변경된 탭 상태 저장
    }
}

/**
 * 드래그 종료 시 호출되는 함수
 * @param {DragEvent} e - 드래그 이벤트 객체
 */
function handleDragEnd(e) {
    e.target.style.opacity = '';  // 투명도 원래대로 복구

    // 모든 탭의 경계선 스타일 초기화
    const tabs = document.getElementById('tabs');
    Array.from(tabs.children).forEach(tab => {
        tab.style.borderLeft = '';
        tab.style.borderRight = '';
    });

    dragTab = null;  // 드래그 중인 탭 참조 초기화
}

/**
 * 탭을 전환하는 함수
 * @param {string} url - 전환할 탭의 URL
 */
function switchTab(url) {
    const tabs = document.getElementById('tabs');
    // 모든 탭의 활성화 상태 제거
    Array.from(tabs.children).forEach(tab => tab.classList.remove('active'));
    // 선택한 탭 활성화
    const activeTab = Array.from(tabs.children).find(tab => tab.getAttribute('data-url') === url);
    if (activeTab) {
        activeTab.classList.add('active');
    }
    // iframe 내용 로드 및 상태 저장
    loadIframe(url);
    saveTabState();
}

/**
 * 탭을 닫는 함수
 * @param {HTMLElement} button - 닫기 버튼 요소
 */
function closeTab(button) {
    const tab = button.parentElement;
    const tabs = document.getElementById('tabs');
    const url = tab.getAttribute('data-url');

    // 이벤트 버블링 방지
    event.stopPropagation();

    // 현재 활성화된 탭인지 확인
    const isActiveTab = tab.classList.contains('active');

    // 탭 제거
    tab.remove();

    // 캐시된 iframe 제거
    if (iframeCache.has(url)) {
        const cachedFrame = iframeCache.get(url);
        cachedFrame.remove();
        iframeCache.delete(url);
    }

    if (isActiveTab && tabs.children.length > 0) {
        const lastTab = tabs.children[tabs.children.length - 1];
        const lastUrl = lastTab.getAttribute('data-url');
        switchTab(lastUrl);
    }
    // 탭 상태 저장
    saveTabState();
}

/**
 * 모든 탭을 닫는 함수
 */
function closeAllTabs() {
    const tabs = document.getElementById('tabs');
    tabs.innerHTML = '';

    // 모든 캐시된 iframe 제거
    iframeCache.forEach((frame, url) => {
        frame.remove();
    });
    iframeCache.clear();

    sessionStorage.removeItem('tabState');
}

/**
 * 새 페이지를 여는 함수
 * @param {string} title - 페이지 제목
 * @param {string} url - 페이지 URL
 */
function openNewPage(title, url) {
    addTab(title, url);
    loadIframe(url);
}

function adjustTabContainerWidth() {
    const iframe = document.getElementById('contentFrame');
    const tabContainer = document.querySelector('.tab-container');
    if (iframe && tabContainer) {
        const iframeWidth = iframe.offsetWidth; // iframe의 실제 너비 계산
        tabContainer.style.width = `${iframeWidth}px`; // 동일한 너비 적용
    }
}

// 페이지 로드 시와 창 크기 변경 시 호출
window.addEventListener('load', adjustTabContainerWidth);
window.addEventListener('resize', adjustTabContainerWidth);

//항목 클릭시 서브메뉴 활성화
function toggleSubmenu(event, submenuId) {
    event.preventDefault(); // 링크 기본 동작 방지
    const submenu = document.getElementById(submenuId);
    if (submenu.style.display === "block") {
        submenu.style.display = "none";
    } else {
        submenu.style.display = "block";
    }
}

// 페이지 로드 시 저장된 탭 상태 복원
document.addEventListener('DOMContentLoaded', () => {
    restoreTabState();
});

/**
 * 로그아웃 시 탭 상태 초기화 함수
 */
function clearTabState() {
    // 세션 스토리지에서 탭 상태 제거
    sessionStorage.removeItem('tabState');

    // 모든 탭 제거
    const tabs = document.getElementById('tabs');
    if (tabs) {
        tabs.innerHTML = '';
    }

    // 모든 캐시된 iframe 제거
    iframeCache.forEach((frame, url) => {
        frame.remove();
    });
    iframeCache.clear();
}



