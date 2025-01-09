function createMemberGrid(memberListGrid){
    const options = {
        width: 'auto',
        minWidth: 100,
        whiteSpace: false,
        resizable: true
    }
    const columns = [
        { header: '멤버 아이디', name: 'memberId' },
        { header: '학과', name: 'deptName' },
        { header: '이름', name: 'name' },
        { header: '전화 번호', name: 'tel' },
        { header: '주소', name: 'address' },
        { header: '생년월일', name: 'birthDate' },
        { header: '계정 상태', name: 'isActive' },
        { header: '생성일', name: 'createAt' },
        { header: '최근 수정일', name: 'updateAt' },
        { header: '개인 이메일', name: 'email' },
        { header: '증명사진', name: 'fileInfo' },
        { header: '성별', name: 'genderStr' },
        { header: '학적 상태', name: 'academicStatusStr' },
        { header: '학년', name: 'gradeStr' },
        { header: '회원 구분', name: 'memberTypeStr' },
        { header: '입학/입사 일자', name: 'startDate' },
        { header: '졸업 일자', name: 'endDate' }];

    fetch('/api/members')
        .then(response =>{
            if(!response.ok){
                throw new Error('Network response was not ok' + response.statusText);
            }

            return response.json();
        })
        .then(data => {
            return data.map(member => ({
                ...member,
                isActive: member.isActive ? '활성화' : '비활성화',
            }));
        })
        .then(mappedData => {
            createGrid(memberListGrid, options, columns, mappedData);
        })
}
function createGrid(memberListGrid, options ,columns, memberList){
    const Grid = tui.Grid;
    const instance = new Grid({
        el: memberListGrid,
        columnOptions: options,
        columns: columns,
        data: memberList,
        scrollX: true,
        bodyHeight: 400,
        bodyWidth: 1300
    })
}