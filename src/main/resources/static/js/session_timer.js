let timeLeft = 1800; // 초기 30분 (1800초)

function updateTimer() {
    if (timeLeft > 0) {
        timeLeft--;
        let minutes = Math.floor(timeLeft / 60);
        let seconds = timeLeft % 60;
        document.getElementById('session-timer').innerHTML = `${minutes}:${seconds}`;
    } else {
        // 세션 만료 처리
        alert("세션이 만료되었습니다.");
        window.location.href = '/logout'; // 로그아웃 페이지로 리다이렉트
    }
}

// 세션 연장 버튼 클릭 시 처리
document.getElementById('session-extension-btn').addEventListener('click', function() {
    fetch('/api/session')
        .then(response => {
            if(response.ok) {
                timeLeft = 1800; // 30분으로 초기화}
            }else{
                alert("세션 연장이 실패하였습니다.");
            }
        })

});

setInterval(updateTimer, 1000); // 1초마다 updateTimer 호출