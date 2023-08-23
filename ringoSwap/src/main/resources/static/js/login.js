$(document).ready(function(){
	
    //id체크 이벤트
    $('#idCheck_Btn').click(memberIdCheck)
})



document.getElementById('signUp').addEventListener('click', function() {
    // 로그인 폼 표시
    document.getElementById('signUpForm').classList.remove('signInHidden');

    // 회원가입 폼 표시
    document.getElementById('loginForm').classList.add('signInHidden');
    
});

function memberIdCheck() {
	let id = $('#user_id').val();
	
	window.open("idCheck?user_id="+id, "ID중복확인", "width=500, height=300, left=150, top=50, scrollbars=no")
}