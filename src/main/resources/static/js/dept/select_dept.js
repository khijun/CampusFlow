let grid = null; // 리스트를 저장해놓기 위한 변수

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
        {header: '학과 코드', name: 'deptId'},
        {header: '학과 이름', name: 'deptName', editor: 'text'},
        {header: '최대 수용 인원', name: 'maxStudents', editor: 'text'},
        {
            header: '학과 상태', name: 'deptStatus',
            editor: {
                type: 'select',
                options: {
                    listItems: [
                        {text: '활성화', value: '활성화'},
                        {text: '비활성화', value: '비활성화'},
                        {text: '대기중', value: '대기중'}
                    ]
                }
            }
        },
        {header: '교양 학점', name: 'generalCredits', editor: 'text'},
        {header: '전공 학점', name: 'majorCredits', editor: 'text'},
        {header: '졸업 학점', name: 'graduationCredits', editor: 'text'},

    ];

    // 필터 객체 정의
    const filter = {
        deptName: deptNameInput && deptNameInput.value ? deptNameInput.value : null,
        minStudentValue: minStudentValueInput && minStudentValueInput.value ? minStudentValueInput.value : null,
        maxStudentValue: maxStudentValueInput && maxStudentValueInput.value ? maxStudentValueInput.value : null,
        deptStatus: deptStatusInput && deptStatusInput.value ? deptStatusInput.value : null,
    };

    fetch('/api/dept?filter=' + encodeURIComponent(JSON.stringify(filter)))
        .then(response => { // 응답을 받으면
            if (!response.ok) { // 응답이 ok인지 확인한다. 아니면 에러를 발생
                throw new Error('Network response was not ok' + response.statusText);
            }

            return response.json(); // json으로 변환한다.
        })
        .then(data => {
            return data.map(dept => ({
                    ...dept,
                    deptStatus: dept.deptStatus === 35 ? '활성화' :
                        dept.deptStatus === 36 ? '비활성화' : '대기중'
                    // 활성35, 비활성36, 대기37
                })
            )
        })
        .then(mappedData => {
            if (grid != null) { // 리스트가 이미 있으면, 그 전껄 지우고 새로운걸 띄우기 위함
                grid.destroy();
                grid = null;
            }
            grid = new tui.Grid({
                el: listDiv,
                columnOptions: {},
                rowHeaders: ['checkbox'], // 체크박스를 사용하여 행 선택
                columns: columns,
                data: mappedData,
                scrollX: true,
                bodyHeight: 400,
                maxBodyWidth: 1300,
            })
        })
}

const deptUpdateBtn = document.querySelector("#dept-update-btn");
deptUpdateBtn.addEventListener('click', () => {
    update();
})

function update() {
    // 버튼 비활성화 코드
    deptUpdateBtn.disabled = true;

    const checkedRows = grid.getCheckedRows(); // 체크된 행 가져오기

    if (checkedRows.length === 0) {
        alert("수정할 행을 선택하세요."); // 체크된 행이 없으면 경고
        deptUpdateBtn.disabled = false;
        return;
    }

    checkedRows.forEach(row => {
        row.deptStatus = row.deptStatus === "활성화" ? 35 :
            row.deptStatus === '비활성화' ? 36 : 37;
    })

    // API 호출
    fetch("/api/dept/all", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(checkedRows) // 체크된 행만 서버로 전송
    })
        .then(response => {
            console.log(checkedRows);
            if (!response.ok) {
                throw new Error("Failed to save subjects");
            }
            return response.json();
        })
}