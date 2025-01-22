const MAX_TABS = 5; // 최대 탭 개수 제한

function addTab(title, url) {
    const tabs = document.getElementById('tabs');
    const existingTab = Array.from(tabs.children).find(tab => tab.getAttribute('data-url') === url);

    // 이미 열려 있는 탭이면 활성화
    if (existingTab) {
        activateTab(existingTab);
        return;
    }

    // 탭 개수 제한
    if (tabs.children.length >= MAX_TABS) {
        alert('탭은 최대 ' + MAX_TABS + '개까지 열 수 있습니다.');
        return;
    }

    // 새 탭 추가
    const newTab = document.createElement('li');
    newTab.className = 'tab-item';
    newTab.setAttribute('data-url', url);
    newTab.innerHTML = `
        <span class="tab-text">${title}</span>
        <button class="close-tab" onclick="closeTab(this, event)">×</button>
    `;
    newTab.onclick = () => switchTab(url);
    tabs.appendChild(newTab);

    // 새 탭 활성화
    activateTab(newTab);
}

function closeTab(button, event) {
    // 이벤트 전파 중지
    event.stopPropagation();

    const tab = button.parentElement;
    const tabs = document.getElementById('tabs');
    const currentUrl = tab.getAttribute('data-url');
    const nextTab = tab.nextElementSibling || tab.previousElementSibling;

    // 탭 제거
    tabs.removeChild(tab);

    // 다음 탭 활성화
    if (nextTab) {
        const nextUrl = nextTab.getAttribute('data-url');
        if (nextUrl === '/main') {
            // 메인 페이지인 경우
            const infoContainer = document.querySelector('.info-container');
            const iframeContainer = document.querySelector('.iframe-container');
            if (infoContainer) infoContainer.style.display = 'flex';
            if (iframeContainer) iframeContainer.style.display = 'none';
        }
        activateTab(nextTab);  // switchTab 대신 activateTab 사용
    } else {
        // 남은 탭이 없으면 메인으로 복귀
        addTab('메인', '/main');  // 메인 탭 새로 추가
    }
}

function closeAllTabs() {
    const tabs = document.getElementById('tabs');
    tabs.innerHTML = '';
    // 기본 페이지로 복귀
    switchTab('dashboard.html');
}

function switchTab(url) {
    const infoContainer = document.querySelector('.info-container');
    const iframeContainer = document.querySelector('.iframe-container');

    // 모든 탭 비활성화
    const tabs = document.getElementById('tabs');
    Array.from(tabs.children).forEach(tab => tab.classList.remove('active'));

    if (url === '/main') {
        // 메인 페이지인 경우
        if (infoContainer) infoContainer.style.display = 'flex';
        if (iframeContainer) iframeContainer.style.display = 'none';
    } else {
        // 다른 페이지인 경우
        if (infoContainer) infoContainer.style.display = 'none';
        if (iframeContainer) {
            iframeContainer.style.display = 'block';
            let iframe = iframeContainer.querySelector('iframe');
            if (!iframe) {
                iframe = document.createElement('iframe');
                iframe.name = 'contentFrame';
                iframeContainer.appendChild(iframe);
            }
            // iframe 소스 설정 전에 로딩 체크
            iframe.onload = function() {
                console.log('iframe loaded:', url);
            };
            iframe.onerror = function() {
                console.error('iframe load failed:', url);
            };
            iframe.src = url;
        }
    }

    // 현재 탭 활성화
    const activeTab = Array.from(tabs.children).find(tab => tab.getAttribute('data-url') === url);
    if (activeTab) {
        activeTab.classList.add('active');
    }
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

//아이프레임 로드
function loadIframe(url) {
    const iframe = document.querySelector('iframe[name="contentFrame"]');
    if (iframe) {
        iframe.src = url;
    }
}

function activateTab(tab) {
    const url = tab.getAttribute('data-url');
    const tabs = document.getElementById('tabs');

    // 모든 탭 비활성화
    Array.from(tabs.children).forEach(t => t.classList.remove('active'));

    // 현재 탭 활성화
    tab.classList.add('active');

    // iframe 내용 로드
    const mainContent = document.getElementById('main-content-container');
    const iframeContainer = document.querySelector('.iframe-container');

    if (url === '/main') {
        // 메인 페이지인 경우
        if (mainContent) mainContent.style.display = 'block';
        if (iframeContainer) iframeContainer.style.display = 'none';
    } else {
        // 다른 페이지인 경우
        if (mainContent) mainContent.style.display = 'none';
        if (iframeContainer) {
            iframeContainer.style.display = 'block';
            let iframe = iframeContainer.querySelector('iframe');
            if (!iframe) {
                iframe = document.createElement('iframe');
                iframe.name = 'contentFrame';
                iframeContainer.appendChild(iframe);
            }
            iframe.src = url;
        }
    }
}