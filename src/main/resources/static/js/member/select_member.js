let grid = null; // 리스트를 저장해놓기 위한 변수

// 조회 버튼
const memberListSelectButton = document.querySelector("#member-list-select-btn");
// 리스트를 띄울 div
const memberListDiv = document.querySelector('#member-list');

// MemberSearchFilter의 내용들
const memberTypeSelect = document.querySelector("#member-type-select");
const nameInput = document.querySelector("#name-input");
const telInput = document.querySelector("#tel-input");
const activeSelect = document.querySelector("#active-select");
const deptSelect = document.querySelector("#dept-select");
const birthStartInput = document.querySelector("#birth-start-input");
const birthEndInput = document.querySelector("#birth-end-input");
const createAtStartInput = document.querySelector("#create-at-start-input");
const createAtEndInput = document.querySelector("#create-at-end-input");
const academicStatusSelect = document.querySelector("#academic-status-select");
const gradeSelect = document.querySelector("#grade-select");
const startDateStartInput = document.querySelector("#start-date-start-input");
const startDateEndInput = document.querySelector("#start-date-end-input");
const endDateStartInput = document.querySelector("#end-date-start-input");
const endDateEndInput = document.querySelector("#end-date-end-input");


memberListSelectButton.addEventListener('click', () => {
    createMemberGrid(memberListDiv);
})

function createMemberGrid(memberListDiv) {
    const columns = [
        {header: '아이디', name: 'memberId', width: 'auto'},
        {header: '학과', name: 'deptName', width: 'auto'},
        {header: '이름', name: 'name', width: 'auto', editor: 'text'},
        {header: '전화 번호', name: 'tel', width: 'auto', editor: 'text'},
        {header: '주소', name: 'address', width: 'auto', editor: 'text'},
        {header: '생년월일', name: 'birthDate', width: 'auto', editor: 'text'},
        {header: '계정 상태', name: 'isActive', width: 'auto', editor: {
                customStyle: false, // 기본 스타일 제거 (적용 가능한 경우)
                type: 'select',
                options: {
                    listItems: [
                        {text: '활성화', value: '활성화'},
                        {text: '비활성화', value: '비활성화'},
                    ]
                }
            }},
        {header: '생성일', name: 'createAt', width: 'auto'},
        {header: '최근 수정일', name: 'updateAt', width: 'auto'},
        {header: '개인 이메일', name: 'email', width: 'auto', editor: 'text'},
        {header: '증명사진', name: 'fileInfo', width: 'auto'},
        {header: '성별', name: 'genderStr', width: 'auto'},
        {header: '학적 상태', name: 'academicStatusStr', width: 'auto'},
        {header: '학년', name: 'gradeStr', width: 'auto'},
        {header: '회원 구분', name: 'memberTypeStr', width: 'auto'},
        {header: '입학/입사 일자', name: 'startDate', width: 'auto', editor: 'text'},
        {header: '졸업 일자', name: 'endDate', width: 'auto', editor: 'text'}];

    // 필터 객체 정의
    const filter = {
        memberType: memberTypeSelect && memberTypeSelect.value ? Number(memberTypeSelect.value) : null,
        name: nameInput && nameInput.value ? nameInput.value : null,
        tel: telInput && telInput.value ? telInput.value : null,
        isActive: activeSelect && activeSelect.value ? activeSelect.value : null,
        deptId: deptSelect && deptSelect.value ? Number(deptSelect.value) : null,
        birthStart: birthStartInput && birthStartInput.value ? birthStartInput.value : null,
        birthEnd: birthEndInput && birthEndInput.value ? birthEndInput.value : null,
        createAtStart: createAtStartInput && createAtStartInput.value ? createAtStartInput.value : null,
        createAtEnd: createAtEndInput && createAtEndInput.value ? createAtEndInput.value : null,
        academicStatus: academicStatusSelect && academicStatusSelect.value ? Number(academicStatusSelect.value) : null,
        grade: gradeSelect && gradeSelect.value ? Number(gradeSelect.value) : null,
        startDateStart: startDateStartInput && startDateStartInput.value ? startDateStartInput.value : null,
        startDateEnd: startDateEndInput && startDateEndInput.value ? startDateEndInput.value : null,
        endDateStart: endDateStartInput && endDateStartInput.value ? endDateStartInput.value : null,
        endDateEnd: endDateEndInput && endDateEndInput.value ? endDateEndInput.value : null,
    };

    fetch('/api/member/all?filter='+encodeURIComponent(JSON.stringify(filter)))
        .then(response => { // 응답을 받으면
            if (!response.ok) { // 응답이 ok인지 확인한다. 아니면 에러를 발생
                throw new Error('Network response was not ok' + response.statusText);
            }

            return response.json(); // json으로 변환한다.
        })
        .then(data => { // 이건 데이터의 형태를 바꾸는 작업. 없으면 스킵해도됨
            return data.map(member => ({
                ...member, // data에 있는 모든 변수들을 그대로 복사해 붙여넣는다.
                updateAt: member.updateAt.trim().replace('T',' '),
                createAt: member.createAt.trim().replace('T',' '),
                isActive: member.isActive ? '활성화' : '비활성화', // boolean 타입을 보기쉽게 변경
            }));
        })
        .then(mappedData => {
            if (grid != null) { // 리스트가 이미 있으면, 그 전껄 지우고 새로운걸 띄우기 위함
                grid.destroy();
                grid = null;
            }
            grid = new tui.Grid({
                el: memberListDiv,
                columnOptions: {
                    resizable: true,
                    minWidth: 100,
            },
                columns: columns,
                data: mappedData,
                scrollX: true,
                bodyHeight: 400,
                maxBodyWidth: 1300,
                editingEvent: 'click',
                rowHeaders: ['checkbox'], // 체크박스를 사용하여 행 선택
            })
        })
}

const memberUpdateBtn = document.querySelector("#member-update-btn");
memberUpdateBtn.addEventListener('click', ()=>{
    updateMember();
})

function updateMember(){
    // 버튼 비활성화 코드
    memberUpdateBtn.disabled = true;

    const checkedRows = grid.getCheckedRows(); // 체크된 행 가져오기

    if (checkedRows.length === 0) {
        alert("수정할 행을 선택하세요."); // 체크된 행이 없으면 경고
        memberUpdateBtn.disabled = false;
        return;
    }

    checkedRows.forEach(row=>{
        row.isActive = row.isActive==="활성화"?true:false;
        row.updateAt = row.updateAt.trim().replace(' ','T');
        row.createAt = row.createAt.trim().replace(' ','T');
    })

    // API 호출
    fetch("/api/member/all", {
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
        .then(data => {
            alert("수정 완료되었습니다");
            createMemberGrid(memberListDiv);
        })
        .catch(error => {
            console.error("Error saving subjects:", error);
            alert("오류가 발생했습니다");
        })
        .finally(data=>{
            memberUpdateBtn.disabled = false;
        });

}