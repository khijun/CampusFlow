const MAX_TABS = 5; // 최대 탭 개수 제한

// iframe 로드 함수: 지정된 URL을 iframe에 로드
function loadIframe(url) {
   const iframe = document.querySelector('iframe[name="contentFrame"]');
   if (iframe) {
      iframe.src = url;
   }
}

// 새로운 탭 추가 함수
function addTab(title, url) {
   const tabs = document.getElementById('tabs');
   // 이미 존재하는 탭인지 확인
   const existingTab = Array.from(tabs.children).find(tab => tab.getAttribute('data-url') === url);

   // 이미 존재하는 탭이면 해당 탭으로 전환만 수행
   if (existingTab) {
      switchTab(url);
      return;
   }

   // 새로운 탭 요소 생성
   const newTab = document.createElement('li');
   newTab.className = 'tab-item';
   newTab.setAttribute('data-url', url);
   // 탭의 HTML 구조 설정 (제목과 닫기 버튼)
   newTab.innerHTML = `
        ${title}
        <button class="close-tab" onclick="closeTab(this)">×</button>
    `;
   // 탭 클릭 이벤트 설정
   newTab.onclick = () => switchTab(url);

   // 탭을 탭 목록에 추가하고 해당 탭으로 전환
   tabs.appendChild(newTab);
   switchTab(url);
}

// 새 페이지를 탭으로 여는 함수
function openNewPage(title, url) {
   addTab(title, url);
   loadIframe(url);
}

// 탭 전환 함수
function switchTab(url) {
   const tabs = document.getElementById('tabs');
   // 모든 탭의 active 클래스 제거
   Array.from(tabs.children).forEach(tab => tab.classList.remove('active'));
   // 선택된 URL에 해당하는 탭 찾기
   const activeTab = Array.from(tabs.children).find(tab => tab.getAttribute('data-url') === url);
   // 찾은 탭에 active 클래스 추가
   if (activeTab) {
      activeTab.classList.add('active');
   }
   // iframe에 해당 URL 로드
   loadIframe(url);
}

// 개별 탭 닫기 함수
function closeTab(button) {
   const tab = button.parentElement;
   const tabs = document.getElementById('tabs');

   // 닫기 버튼 클릭 시 탭 클릭 이벤트가 발생하지 않도록 방지
   event.stopPropagation();

   // 현재 닫으려는 탭이 활성화된 탭인지 확인
   const isActiveTab = tab.classList.contains('active');

   // 탭 제거
   tab.remove();

   // 활성화된 탭을 닫은 경우, 남은 마지막 탭을 활성화
   if (isActiveTab && tabs.children.length > 0) {
      const lastTab = tabs.children[tabs.children.length - 1];
      const url = lastTab.getAttribute('data-url');
      switchTab(url);
   }

   // 모든 탭이 닫힌 경우 대시보드 탭 생성
   if (tabs.children.length === 0) {
      openNewPage('대시보드', 'dashboard.html');
   }
}

// 모든 탭 닫기 함수
function closeAllTabs() {
   const tabs = document.getElementById('tabs');
   // 모든 탭 제거
   tabs.innerHTML = '';
   // 대시보드 탭 생성
   openNewPage('대시보드', 'dashboard.html');
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

