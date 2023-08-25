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
    //인증코드 전송 시 DB에 이메일이 존재하지 않는 경우 안내메시지 출력
    $('.sendCode1').click(sendVerificationCode)
    $('.sendCode2').click(sendVerificationCode)
    $('.sendCode3').click(sendVerificationCode)
    //이메일 인증 코드 맞는지 확인, 안내메시지 출력
    $('.confirmCode1').click(checkVerificationCode)
    $('.confirmCode2').click(checkVerificationCode)
    $('.confirmCode3').click(checkVerificationCode)
    //인증 코드가 맞으면 이메일에 맞는 아이디 출력
    $('#checkIdBtn').click(paintID)
})


function memberIdCheck() {
	let id = $('#user_id').val();
	
	window.open("idCheck?user_id="+id, "ID중복확인", "width=500, height=300, left=150, top=50, scrollbars=no")
}


function sendVerificationCode() {
	("이메일이 존재 하는지 안 하는지 아직 모름")
}

function checkVerificationCode() {
	
    const correctCode = "123456";
    // input 값 가져오기 위해 .value를 추가했습니다.
    const userCode1 = document.getElementById("checkCode1").value;
    const userCode2 = document.getElementById("checkCode2").value;
    const userCode3 = document.getElementById("checkCode3").value;
	const resultMessage1 = document.querySelector(".resultMessage1");
	const resultMessage2 = document.querySelector(".resultMessage2");
	const resultMessage3 = document.querySelector(".resultMessage3");


    if (userCode1 === correctCode 
    || userCode2 === correctCode 
    || userCode3 === correctCode) {
        resultMessage1.innerText = "코드가 일치합니다.";
        resultMessage1.style.color = "black"; // 색상을 블랙으로 초기화합니다.
        resultMessage2.innerText = "코드가 일치합니다.";
        resultMessage2.style.color = "black"; // 색상을 블랙으로 초기화합니다.
        resultMessage3.innerText = "코드가 일치합니다.";
        resultMessage3.style.color = "black"; // 색상을 블랙으로 초기화합니다.
    } else {
        resultMessage1.innerText = "코드가 일치하지 않습니다.";
        resultMessage1.style.color = "red";
        resultMessage2.innerText = "코드가 일치하지 않습니다.";
        resultMessage2.style.color = "red";
        resultMessage3.innerText = "코드가 일치하지 않습니다.";
        resultMessage3.style.color = "red";
    }
}

function paintID() {
	const dbEmails = {
		user_id: "taeho",
		email: "h4@example.com"
	}; 
	
	const emailInput = document.getElementById("sendCode2").value;
	const paintID = document.querySelector(".paintID");
	
	if(resultMessage.innerText === "코드가 일치합니다.") {
        paintID.innerText = `이메일에 해당하는 ID는 ${dbEmails.user_id} 입니다.`;
        paintID.style.color = "black";
    } 
}
