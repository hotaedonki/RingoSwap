document.getElementById('signUp').addEventListener('click', function() {
    // 로그인 폼 표시
    document.getElementById('signUpForm').classList.remove('signInHidden');

    // 회원가입 폼 표시
    document.getElementById('loginForm').classList.add('signInHidden');
});

