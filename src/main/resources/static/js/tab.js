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
        ${title}
        <button class="close-tab" onclick="closeTab(this)">×</button>
    `;
   newTab.onclick = () => switchTab(url);
   tabs.appendChild(newTab);

   // 새 탭 활성화
   activateTab(newTab);
}

function closeTab(button) {
   const tab = button.parentElement;
   const tabs = document.getElementById('tabs');
   const nextTab = tab.nextElementSibling || tab.previousElementSibling;

   // 탭 제거
   tabs.removeChild(tab);

   // 다음 탭 활성화
   if (nextTab) {
      switchTab(nextTab.getAttribute('data-url'));
      nextTab.classList.add('active');
   } else {
      // 기본 페이지로 복귀
      switchTab('dashboard.html');
   }
}

function closeAllTabs() {
   const tabs = document.getElementById('tabs');
   tabs.innerHTML = '';
   // 기본 페이지로 복귀
   switchTab('dashboard.html');
}

function switchTab(url) {
   const tabs = document.getElementById('tabs');
   Array.from(tabs.children).forEach(tab => tab.classList.remove('active'));
   const activeTab = Array.from(tabs.children).find(tab => tab.getAttribute('data-url') === url);
   if (activeTab) {
      activeTab.classList.add('active');
   }
   document.getElementById('contentFrame').src = url;
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

console.log('main-content:', document.querySelector('.main-content').offsetHeight);
console.log('iframe:', document.querySelector('iframe').offsetHeight);
