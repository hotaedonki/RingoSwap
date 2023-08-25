$(document).ready(function() {
    // Event listeners
    $('#searchID').click(showSearchID);
    $('#searchPW').click(showSearchPW);
    $('#signUp').click(showSignUpForm);
    $('.Resgister_btn').click(joinCheck);
    $('#idCheck_Btn').click(memberIdCheck);
    $('.sendCode').click(emailConfirm);
    $('.sendCodeForPassword').click(emailConfirmForPassword);
    $('.confirmCode').click(searchIDCode);
    $('#checkIdBtn').click(paintID);

    function showSignUpForm() {
        $('#loginForm').addClass("signInHidden");
        $('#signUpForm').removeClass("signInHidden");
    }

    function showSearchID() {
        $('#loginForm').addClass("signInHidden");
        $('#paintSearchID').removeClass("signInHidden");
    }

    function showSearchPW() {
        $('#loginForm').addClass("signInHidden");
        $('#paintSearchPW').removeClass("signInHidden");
    }

    function memberIdCheck() {
        let id = $('#user_id').val();
        window.open("idCheck?user_id=" + id, "ID중복확인", "width=500, height=300, left=150, top=50, scrollbars=no");
    }

    function emailConfirm() {
        const btn = event.target;
		const dataId = btn.getAttribute('data-id');
		let email = $('#email' + dataId).val();
		alert(`${email}로 인증코드를 전송하였습니다.`);
		
        $.ajax({
            url: 'emailConfirm',
            type: 'post',
            data: { email: email },
            success: function() {
                
            }
        });
    }

    function emailConfirmForPassword() {
        let id = $('#idForPassword').val();
        let email = $('#emailForPassword').val();

        $.ajax({
            url: 'emailConfirmForPassword',
            type: 'post',
            data: { user_id: id, email: email },
            success: function(response) {
                if (response) {
                    alert(`${email}로 인증코드를 전송하였습니다.`);
                } else {
                    alert('존재하지 않는 ID거나 Email입니다.');
                }
            }
        });
    }

    function searchIDCode() {
        const btn = event.target;
	    const dataId = btn.getAttribute('data-id');
	    const userCode = $("#code" + dataId).val();
	    const resultMessage = $(".resultMessage" + dataId);

        $.ajax({
            url: 'checkverifycode',
            type: 'post',
            data: { code: userCode },
            dataType: 'json',
            success: function(response) {
                switch (response) {
                    case 'VERIFIED':
                        resultMessage.text("코드가 일치합니다.").css("color", "black");
                        break;
                    case 'INCORRECT':
                    case 'CHECKINPUT':
                        resultMessage.text("코드가 일치하지 않습니다.").css("color", "red");
                        break;
                    default:
                        resultMessage.text("알 수 없는 오류 발생.").css("color", "red");
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
        
        const resultMessage = $(".resultMessage2");
        const paintIDElement = $(".paintID");
        
        if (resultMessage.text() === "코드가 일치합니다.") {
            paintIDElement.text(`이메일에 해당하는 ID는 ${dbEmails.user_id} 입니다.`).css("color", "black");
        }
    }

    function joinCheck() {
        const user_id = $('#user_id').val();
        const password = $('#password').val();
        const confirmPassword = $('#confirmPassword').val();
        const first_name = $('#first_name').val();
        const last_name = $('#last_name').val();
        const birth_date = new Date($('#birth_date').val());
        const currentYear = new Date().getFullYear();
        const errorSpan = $("#passwordErr");
        
        errorSpan.text("");  // Initialize error message

        if (user_id.length < 4 || user_id.length > 10) {
            alert('ID는 3 ~ 10자로 입력해주세요.');
            return false;
        }

        if (password !== confirmPassword) {
            errorSpan.text("비밀번호가 일치하지 않습니다.").css("color", "red");
            return false;
        } else {
            errorSpan.text("비밀번호가 일치합니다.").css("color", "black");
        }

        if (first_name == '') {
            alert('성을 입력해주세요.');
            return false;
        }

        if (last_name == '') {
            alert('이름을 입력해주세요.');
            return false;
        }

        if (currentYear - birth_date.getFullYear() > 110) {
            alert("날짜를 다시 입력해주세요.");
            return false;
        }

        if (errorSpan.text() === "비밀번호가 일치합니다.") {
            return true;
        }
    }
});