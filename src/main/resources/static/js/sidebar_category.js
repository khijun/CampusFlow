document.addEventListener('DOMContentLoaded', function() {
    fetch('/category')
        .then(response => response.json())
        .then(categories => {
            // 사이드바 생성
            createSidebar(categories);
        })
        .catch(error => {
            console.error("Fetch 요청 실패:", error);
        });

    // 카테고리 항목을 받아와 사이드바를 생성하는 함수
    function createSidebar(categories) {
        // 사이드바 요소
        const sidebar = document.getElementById('side-bar');

        // 추가할 메뉴
        const sidebarMenu = document.createElement('ul');
        sidebarMenu.classList.add('sidebar-menu');

        categories.forEach(category => {
            // 최상위 카테고리 항목 생성
            const categoryItem = document.createElement('li');
            const categoryLink = document.createElement('a');
            categoryLink.setAttribute('href', '#');
            categoryLink.classList.add('menu-item');
            categoryLink.innerHTML = category.image
                ? `<img src="/file/${category.image}" alt="${category.name}" style="width: 20px; height: 20px;"> ${category.name}`
                : `${category.name}`;

            // 상위 카테고리 클릭 시 하위 카테고리 보이기/숨기기 기능 추가
            categoryLink.addEventListener('click', function(event) {
                event.preventDefault(); // 링크 이동을 방지
                const subMenu = categoryItem.querySelector('.submenu'); // 하위 메뉴 찾기
                if (subMenu) {
                    subMenu.style.display = (subMenu.style.display === 'block') ? 'none' : 'block';
                }
            });

            categoryItem.appendChild(categoryLink);

            // 자식 항목이 있으면 재귀적으로 추가
            if (category.children && category.children.length > 0) {
                const subMenu = document.createElement('ul');
                subMenu.classList.add('submenu');
                subMenu.style.display = 'none'; // 기본적으로 숨기기
                createSubMenu(category.children, subMenu); // 자식 항목 처리
                categoryItem.appendChild(subMenu);
            }

            sidebarMenu.appendChild(categoryItem);
        });

        sidebar.appendChild(sidebarMenu);
    }

    // 자식 항목을 추가하는 함수
    function createSubMenu(children, parentElement) {
        children.forEach(child => {
            const childItem = document.createElement('li');
            const childLink = document.createElement('a');
            childLink.setAttribute('href', child.url || '#'); // 자식 항목의 url 사용
            childLink.innerHTML = child.image
                ? `<img src="/file/${child.image}" alt="${child.name}" style="width: 20px; height: 20px;"> ${child.name}`
                : `${child.name}`;

            childItem.appendChild(childLink);
            parentElement.appendChild(childItem);

            // 자식이 또 있으면 재귀적으로 처리
            if (child.children && child.children.length > 0) {
                const subSubMenu = document.createElement('ul');
                subSubMenu.classList.add('submenu');
                subSubMenu.style.display = 'none'; // 기본적으로 숨기기
                createSubMenu(child.children, subSubMenu); // 자식의 자식 처리
                childItem.appendChild(subSubMenu);
            }
        });
    }
});
