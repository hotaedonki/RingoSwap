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


$(document).ready(function(){
    //id체크 이벤트
    $('#idCheck_Btn').click(memberIdCheck)
    //인증코드 전송 시 DB에 이메일이 존재하지 않는 경우 안내메시지 출력
    $('.sendCode').click(emailConfirm);
    $('.confirmCode').click(checkVerificationCode);
    $('#checkIdBtn').click(paintID);
    
    // 각 버튼을 클릭하면 위 함수 이벤트가 실행됨
	document.getElementById('searchID').addEventListener('click', showSearchID);
	document.getElementById('searchPW').addEventListener('click', showSearchPW);
	document.getElementById('signUp').addEventListener('click', showSignUpForm);
});


function memberIdCheck() {
	let id = $('#user_id').val();
	
	window.open("idCheck?user_id="+id, "ID중복확인", "width=500, height=300, left=150, top=50, scrollbars=no")
}


function emailConfirm(event) {
		const btn = event.target;
		const dataId = btn.getAttribute('data-id');
		let email = $('#email' + dataId).val();
		alert(`${email}로 인증코드를 전송하였습니다.`);
				
		$.ajax({
			url:'emailConfirm'
			, type:'post'
			, data:{email: email}
			, success: function()
			{
				console.log("성공");
			}
		});
}

const EmailVerifyState = {
    VERIFIED: 2, 
    INCORRECT: 1,
    CHECKINPUT: 0
};

function checkVerificationCode(event) {
    const btn = event.target;
    const dataId = btn.getAttribute('data-id');
    const userCode = document.getElementById("code" + dataId).value;
    const resultMessage = document.querySelector(".resultMessage" + dataId);

    $.ajax({
        url: 'checkverifycode',
        type: 'post',
        data: { code: userCode },
        success: function(response) {
            switch (response) {
                case EmailVerifyState.VERIFIED:
                    resultMessage.innerText = "코드가 일치합니다.";
                    resultMessage.style.color = "black";
                    break;
                case EmailVerifyState.INCORRECT:
                case EmailVerifyState.CHECKINPUT:
                    resultMessage.innerText = "코드가 일치하지 않습니다.";
                    resultMessage.style.color = "red";
                    break;
                default:
                    resultMessage.innerText = "알 수 없는 오류 발생.";
                    resultMessage.style.color = "red";
                    break;
            }
        }
    });
}

function paintID() {
	const dbEmails = {
		user_id: "taeho",
		email: "h4@example.com"
	}; 
	
	const resultMessage = document.querySelector(".resultMessage2");
	const paintID = document.querySelector(".paintID");
	
	if(resultMessage.innerText === "코드가 일치합니다.") {
        paintID.innerText = `이메일에 해당하는 ID는 ${dbEmails.user_id} 입니다.`;
        paintID.style.color = "black";
    }
}