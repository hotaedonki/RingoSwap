document.getElementById('login').addEventListener('click', function() {
    // 로그인 폼 표시
    document.getElementById('loginForm').classList.remove('hidden');

    // clock, Welcome 문구 및 kiminonawa 숨기기
    document.getElementById('clock').classList.add('hidden');
    document.querySelector('h1').classList.add('hidden');
    document.getElementById('kiminonawa').classList.add('hidden');
});


/*
document.getElementById('signUp').addEventListener('click', function() {
    // 로그인 폼 숨기기
    document.getElementById('loginForm').classList.add('hidden');
    
    // 회원가입 폼 표시
    document.getElementById('signUpForm').classList.remove('hidden');
});

// 회원가입 폼 데이터를 AJAX로 서버에 전송
document.getElementById('ajaxSignUpForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    let formData = new FormData(this);
    
    fetch('/member/idCheck', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        // 예: 서버에서 { success: true, message: '회원가입 성공!' }와 같은 응답을 반환한다고 가정
        if(data.success) {
            alert(data.message);
            // 다른 로직 (예: 로그인 페이지로 리다이렉트)
        } else {
            alert(data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('오류가 발생했습니다. 다시 시도해 주세요.');
    });
});
*/ 