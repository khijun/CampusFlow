// 선택된 과목과 선수강 과목 ID를 저장
let selectedSubjectId = null; // 선택된 과목 ID
let selectedPrereqSubjectId = null; // 선택된 선수강 과목 ID

function searchSubjects(inputId, selectId, excludeId = null) {
    const keyword = document.getElementById(inputId).value;
    const select = document.getElementById(selectId);

    if (keyword.length > 0) {
        fetch(`/iframe/curriculum/subjects?keyword=${encodeURIComponent(keyword)}`)
            .then(response => response.json())
            .then(data => {
                // 현재 선택된 값 저장
                const currentSelectedId = select.value;

                // `<select>` 초기화
                select.innerHTML = '<option value="" disabled>검색 결과를 선택하세요</option>';

                data.forEach(subject => {
                    // 제외할 ID가 있는 경우 필터링
                    if (excludeId !== null && subject.subjectId === excludeId) return;

                    const option = document.createElement("option");
                    option.value = subject.subjectId;
                    option.textContent = `${subject.subjectName} (${subject.subjectCredits}학점)`;

                    // 이전 선택값 유지
                    if (subject.subjectId == currentSelectedId) {
                        option.selected = true;
                    }

                    select.appendChild(option);
                });

                // 기본 상태 설정
                if (!currentSelectedId) {
                    select.value = "";
                }
            })
            .catch(error => console.error("Error fetching subjects:", error));
    } else {
        // 검색어가 없을 경우 초기화
        select.innerHTML = '<option value="" disabled selected>검색 결과가 여기에 표시됩니다</option>';
    }
}

// 과목 선택 이벤트 처리
document.getElementById("subjectName-addform").addEventListener("change", function () {
    selectedSubjectId = parseInt(this.value); // 선택된 과목 ID 저장
    searchSubjects("subjectName-search", "prereqSubject-addform", selectedSubjectId); // 선수강 과목에서 선택된 과목 제외
});

// 선수강 과목 선택 이벤트 처리
document.getElementById("prereqSubject-addform").addEventListener("change", function () {
    selectedPrereqSubjectId = parseInt(this.value); // 선택된 선수강 과목 ID 저장
});
