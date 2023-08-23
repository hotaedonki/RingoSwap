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


$(document).ready(function(){
	
    //id체크 이벤트
    $('#idCheck_Btn').click(memberIdCheck)
})


function memberIdCheck() {
	let id = $('#user_id').val();
	
	window.open("idCheck?user_id="+id, "ID중복확인", "width=500, height=300, left=150, top=50, scrollbars=no")
}
