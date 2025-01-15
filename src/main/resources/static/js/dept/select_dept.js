let instance = null; // 리스트를 저장해놓기 위한 변수

// 조회 버튼
const listSelectButton = document.querySelector("#dept-list-select-btn");
// 리스트를 띄울 div
const listDiv = document.querySelector('#dept-list');

// SearchFilter의 내용들
const deptNameInput = document.querySelector("#dept-name-input");
const minStudentValueInput = document.querySelector("#dept-min-student-value-input");
const maxStudentValueInput = document.querySelector("#dept-max-student-value-input");
const deptStatusInput = document.querySelector("#dept-status-input");

listSelectButton.addEventListener('click', () => {
    createDeptGrid(listDiv);
})

function createDeptGrid(listDiv) {
    const columns = [
        {header: '학과 코드', name: 'deptId', width: 'auto'},
        {header: '학과 이름', name: 'deptName', width: 'auto'},
        {header: '최대 수용 인원', name: 'maxStudents', width: 'auto'},
        {header: '학과 상태', name: 'deptStatus', width: 'auto'},
    ];

    // 필터 객체 정의
    const filter = {
        deptName: deptNameInput && deptNameInput.value ? deptNameInput.value : null,
        minStudentValue: minStudentValueInput && minStudentValueInput.value ? minStudentValueInput.value : null,
        maxStudentValue: maxStudentValueInput && maxStudentValueInput.value ? maxStudentValueInput.value : null,
        deptStatus: deptStatusInput && deptStatusInput.value ? deptStatusInput.value : null,
    };

    fetch('/api/dept?filter='+encodeURIComponent(JSON.stringify(filter)))
        .then(response => { // 응답을 받으면
            if (!response.ok) { // 응답이 ok인지 확인한다. 아니면 에러를 발생
                throw new Error('Network response was not ok' + response.statusText);
            }

            return response.json(); // json으로 변환한다.
        })
        .then(data => {
            return data;// 이건 데이터의 형태를 바꾸는 작업. 없으면 스킵해도됨
        })
        .then(mappedData => {
            if (instance != null) { // 리스트가 이미 있으면, 그 전껄 지우고 새로운걸 띄우기 위함
                instance.destroy();
                instance = null;
            }
            instance = createGrid(listDiv, columns, mappedData, null);
        })
}