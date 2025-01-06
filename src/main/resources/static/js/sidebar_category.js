document.addEventListener('DOMContentLoaded', function() {
    fetch('/category') // 서버에서 카테고리 데이터를 받는 URL
        .then(response => response.json()) // JSON 형식으로 응답 받기
        .then(categories => {
            createSidebar(categories); // 카테고리 데이터로 사이드바 생성
        })
        .catch(error => {
            console.error("Fetch 요청 실패:", error);
        });

    // 카테고리 항목을 동적으로 생성하는 함수
    function createSidebar(categories) {
        const sidebar = document.getElementById('side-bar'); // 카테고리 리스트를 삽입할 div

        categories.forEach(category => {
            // 최상위 카테고리 항목 생성
            const categoryElement = document.createElement('a');
            categoryElement.setAttribute('href', category.url || '#'); // url이 없을 경우 #로 대체
            // 이미지가 없으면 이미지 태그를 만들지 않음
            if (category.image) {
                categoryElement.innerHTML = `<img src="/file/${category.image}" alt='${category.name}' style="width: 20px; height: 20px;"> ${category.name}`;
            } else {
                categoryElement.innerHTML = `${category.name}`;
            }

            // 상위 카테고리 클릭 시 하위 카테고리 보이기/숨기기 기능 추가
            categoryElement.addEventListener('click', function(event) {
                event.preventDefault(); // 링크 이동을 방지
                const subMenu = categoryElement.nextElementSibling; // 하위 메뉴 찾기
                if (subMenu) {
                    // 하위 메뉴의 display 속성을 토글
                    subMenu.style.display = (subMenu.style.display === 'block') ? 'none' : 'block';
                }
            });

            sidebar.appendChild(categoryElement);

            // 자식 항목이 있으면 재귀적으로 추가
            if (category.children && category.children.length > 0) {
                const subMenu = document.createElement('div');
                subMenu.classList.add('submenu');
                subMenu.style.display = 'none'; // 기본적으로 숨기기
                sidebar.appendChild(subMenu);
                createSubMenu(category.children, subMenu); // 자식 항목 처리
            }
        });
    }

    // 자식 항목을 추가하는 함수
    function createSubMenu(children, parentElement) {
        children.forEach(child => {
            const childElement = document.createElement('a');
            childElement.setAttribute('href', '#'); // 자식 항목은 #로 링크

            // 이미지가 있으면 이미지, 없으면 그냥 텍스트만 표시
            if (child.image) {
                childElement.innerHTML = `<img src="/file/${child.image}" alt='${child.name}' style="width: 20px; height: 20px;"> ${child.name}`;
            } else {
                childElement.innerHTML = `${child.name}`;
            }

            parentElement.appendChild(childElement);

            // 자식이 또 있으면 재귀적으로 처리
            if (child.children && child.children.length > 0) {
                const subSubMenu = document.createElement('div');
                subSubMenu.classList.add('submenu');
                parentElement.appendChild(subSubMenu);
                createSubMenu(child.children, subSubMenu); // 자식의 자식 처리
            }
        });
    }
});
