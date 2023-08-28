$(document).ready(function()
{
	$('#checkEmailBtn').click(emailConfirm);
	$('#checkVerifyCodeBtn').click(checkVerifyCode);
	
	
   	window.addEventListener('beforeunload', (event) => 
	{
		// 표준에 따라 기본 동작 방지
	 	event.preventDefault();
	 	
		// Chrome에서는 returnValue까지 값을 줘야 활성화 됨
		event.returnValue = '';
	});
	
	/*
	$(window).on('beforeunload', function(event) 
	{
		event.preventDefault();
    	return '이 페이지를 벗어나면 현재 작성중인 내용이 지워집니다. 응애';
    });
    */
	
	// 현재 저장된 세션 클리어 & 저장된 세션 길이 확인용
	//sessionStorage.clear();
	//alert(sessionStorage.length);
});

// 아이디 유호성 검사 및 중복 검사
function idCheck()
{
	const user_id = document.getElementById('user_id').value;
	
	if (user_id.length <= 1)
	{
		alert("id의 길이는 2글자 이상이어야 합니다.");
		return false;
	}
	
	$.ajax({
		url:'idCheck'
		, type:'post'
		, data:{user_id:user_id}
		, success: function(result)
		{
			if (!result)
			{
				alert("이미 존재하는 ID입니다. 다른 ID를 사용해주세요.");
				return;
			}
			
			alert("사용 가능한 ID입니다.");
		}
		, error: function()
		{
			alert("해당 요청 실행을 실패했습니다.");
		}
	});
}


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
		, error: function()
		{
			let formCheck = checkEmailForm();
			
			if (formCheck)
			{
				alert("인증번호 전송 실패하였습니다.");
			}
		}
	});
}

//인증 번호 확인
function checkVerifyCode()
{
	let verifyCode = $('#verifyCode').val();
	
	$.ajax({
		url: 'checkverifycode'
		, type:'post'
		, data:{code:verifyCode}
		, datatype:'json'
		, success: function(state)
		{
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
		, error: function(e)
		{
			alert(e);
		}
	});	
}

// 입력 폼 유효성 검사
function checkEmailForm()
{
	let email = $('#email').val();
	
	if (email.length <= 4 || !email.includes('@'))
	{
		alert("잘못된 형식의 이메일입니다. 다시 확인해주세요.");
		return false;
	}
	
	return true;
}

// 세션 클리어
function removeAllSessionJoin()
{
	$.ajax({
		url: 'removeAllSessionJoin'
		, type:'post'
		, success: function()
		{
			alert("세션 제거 완료")
		}
		, error: function(e)
		{
			alert(e);
		}
	});
}

function checkSession()
{
	$.ajax({
		url:'checkSession'
		, type:'post'
		, datatype:'json'
		, success: function(state)
		{
			switch (state)
			{
				case 'CHECKID':
					alert("ID 중복확인을 해주세요.");
					return false;
				case 'CHECKEMAIL':
					alert("이메일 인증코드 확인을 해주세요.");
					return false;
				case 'SUCCESS':
					alert("가입을 진심으로 축하드립니다.");
					return true;
			}
		}
	});
}