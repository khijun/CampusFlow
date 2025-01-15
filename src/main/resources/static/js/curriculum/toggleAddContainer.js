// 신규 교육과정 추가 컨테이너를 보이거나 숨기는 함수
function toggleAddContainer() {
    const addContainer = document.querySelector('.add-container');

    if (addContainer) {
        // 현재 컨테이너가 숨겨져 있는 경우 보여줌
        if (addContainer.style.display === 'none' || addContainer.style.display === '') {
            addContainer.style.display = 'block';
        }
        // 현재 컨테이너가 보이는 경우 숨김
        else {
            addContainer.style.display = 'none';
        }
    } else {
        console.error("Add container element not found!");
    }
}

// 페이지가 로드된 후에 이벤트를 연결
document.addEventListener('DOMContentLoaded', () => {
    // 신규 버튼에 클릭 이벤트 연결
    const newButton = document.querySelector('.button-group button[type="button"]');
    if (newButton) {
        newButton.addEventListener('click', toggleAddContainer);
    } else {
        console.error("New button not found!");
    }
});
