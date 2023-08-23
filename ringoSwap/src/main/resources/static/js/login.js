// 회원가입 폼을 보여줌 
function showSignUpForm() {
    document.getElementById('loginForm').classList.add("signInHidden");
    document.getElementById('signUpForm').classList.remove("signInHidden");
}

// 아이디 찾기 폼을 보여줌
function showSearchID() {
    document.getElementById('loginForm').classList.add("signInHidden");
    document.getElementById('paintSearchID').classList.remove("signInHidden");
}

// 비밀번호 찾기 폼을 보여줌
function showSearchPW() {
    document.getElementById('loginForm').classList.add("signInHidden");
    document.getElementById('paintSearchPW').classList.remove("signInHidden");
}

// 각 버튼을 클릭하면 위 함수 이벤트가 실행됨
document.getElementById('searchID').addEventListener('click', showSearchID);
document.getElementById('searchPW').addEventListener('click', showSearchPW);
document.getElementById('signUp').addEventListener('click', showSignUpForm);
