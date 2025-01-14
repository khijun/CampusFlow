// 과목 검색 기능
function searchSubjects(inputElement, datalistId) {
    const keyword = inputElement.value.trim();
    const datalist = document.getElementById(datalistId);

    if (keyword.length > 0) {
        // 서버에 검색 요청
        fetch(`/iframe/curriculum/subjects?keyword=${encodeURIComponent(keyword)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to fetch subjects");
                }
                return response.json();
            })
            .then(data => {
                // `<datalist>` 초기화
                datalist.innerHTML = "";

                // 검색 결과 추가
                data.forEach(subject => {
                    const option = document.createElement("option");
                    option.value = subject.subjectName; // 과목명 표시
                    option.dataset.id = subject.subjectId; // 과목 ID 저장
                    datalist.appendChild(option);
                });
            })
            .catch(error => {
                console.error("Error fetching subjects:", error);
                alert("과목 검색 중 문제가 발생했습니다.");
            });
    } else {
        datalist.innerHTML = ""; // 검색어가 없을 경우 초기화
    }
}

// 과목 선택 이벤트
document.getElementById("subjectSearch").addEventListener("change", function () {
    const selectedOption = Array.from(document.querySelectorAll("#subjectOptions option")).find(option => option.value === this.value);

    if (selectedOption) {
        document.getElementById("subjectId").value = selectedOption.dataset.id;
    } else {
        document.getElementById("subjectId").value = ""; // 선택한 값이 없다면 초기화
    }
});

function addSubjectFromRow() {
    const subjectInput = document.getElementById("subjectSearch");
    const semesterSelect = document.querySelector("#add-row select[name='semesters[]']");
    const subjectIdInput = document.getElementById("subjectId");

    // 입력 값 검증
    const subjectName = subjectInput.value.trim();
    const semester = semesterSelect.value;
    const subjectId = subjectIdInput.value;

    if (!subjectName || !semester || !subjectId) {
        alert("과목명과 학기를 모두 입력하세요.");
        return;
    }

    // 테이블 본문 선택
    const tableBody = document.querySelector("#subject-table tbody");

    // 새 행 추가
    const row = document.createElement("tr");
    row.innerHTML = `
        <td>${subjectName}</td>
        <td>${semester === "FIRST_SEMESTER" ? "1학기" :
        semester === "SECOND_SEMESTER" ? "2학기" :
            semester === "SUMMER" ? "여름학기" : "겨울학기"}</td>
        <td><button type="button" onclick="removeRow(this)">삭제</button></td>
        <input type="hidden" name="subjectIds[]" value="${subjectId}">
        <input type="hidden" name="semesters[]" value="${semester}">
    `;
    tableBody.insertBefore(row, document.getElementById("add-row"));

    // 입력 필드 초기화
    subjectInput.value = "";
    semesterSelect.value = "";
    subjectIdInput.value = "";
}

// 행 삭제 함수
function removeRow(button) {
    const row = button.closest("tr");
    row.remove();
}

// 폼 제출 전 add-row 행 제거
document.querySelector(".add-form").addEventListener("submit", function (event) {
    const addRow = document.getElementById("add-row");
    if (addRow) {
        addRow.remove();
    }
});