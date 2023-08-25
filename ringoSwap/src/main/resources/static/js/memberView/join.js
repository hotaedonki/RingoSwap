$(document).ready(function()
{
	$('#checkEmailBtn').click(emailConfirm);
	$('#checkVerifyCodeBtn').click(checkVerifyCode);
});

//E-mail 전송
function emailConfirm()
{
	let email = $('#email').val();
	
	$.ajax({
		url:'emailConfirm'
		, type:'post'
		, data:{email: email}
		, success: function()
		{
			alert("인증번호를 해당 이메일로 보냈습니다.");
		}
	});
}

//인증 번호 확인
function checkVerifyCode()
{
	let email = $('#email').val();
	let verifyCode = $('#verifyCode').val();
	
	$.ajax({
		url: 'checkVerifyCode'
		, type:'post'
		, data:{code:verifyCode}
		, datatype:'json'
		, success: function(state)
		{
			if (email.length <= 5)
			{
				alert("이메일을 입력해주세요.");
				return;
			}
			
			switch (state)
			{
				case 'CHECKINPUT':
					alert("인증코드부터 전송해주시기 바랍니다.");
					return;
					
				case 'INCORRECT':
					alert("인증번호가 틀립니다.");
					return;
					
				case 'VERIFIED':
					alert("인증되었습니다.");
					return;
			}
		}
	});	
}

function