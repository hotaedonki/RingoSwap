$(document).ready(function()
{
	$('#checkEmailBtn').click(emailConfirm);
	//$('#checkCodeBtn').click(emailCheck);
});

//EMAIL 인증 함수
function emailConfirm()
{
		let email = $('#email').val();
	
		$.ajax({
			url:'emailConfirm'
			, type:'post'
			, data:{email: email}
			, success: function()
			{
				alert("성공");
			}
		});
}

//인증코드체크시 인증코드 확인 함수
/*
function emailCheck(){
		let email = $('#email').val();
		let code = $('#emailConfirm').val();
		
		$.ajax({
			url:'emailCheck'
			, type:'post'
			, data:{email: email}
			, success: function()
			{
				alert("성공");
			}
		})
}
*/