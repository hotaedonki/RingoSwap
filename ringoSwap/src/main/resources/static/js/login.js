$(document).ready(function() {
	const passwordInputs = $('#password, #confirmPassword, #newPassword, #confirmNewPassword');
    const doNotMatchPassword = $(".doNotMatchPassword");
    
    $('#searchID').click(() => toggleVisibility('#loginForm', '#paintSearchID')); // 아이디 찾기 폼을 보여준다
    $('#searchPW').click(() => toggleVisibility('#loginForm', '#paintSearchPW')); // 비밀번호 찾기 폼을 보여준다
    $('.PWcheckbtn').click(gotoResetPasswordPage); // 비밀번호 재설정 폼을 보여준다
    $('.resetPassword').click(resetPassword) // 비밀번호를 재설정한다.
    $('#signUp').click(() => toggleVisibility('#loginForm', '#signUpForm')); // 회원가입 폼을 보여준다
    $('.Resgister_btn').click(joinCheck); // 회원가입을 완료한다
    $('#idCheck_Btn').click(memberIdCheck); // 아이디 중복확인
    $('.sendCode').click(emailConfirm); // 존재하는 이메일인지 확인하고, 인증코드를 전송한다.
    $('.sendCodeForPassword').click(emailConfirmForPassword); // 존재하는 아이디와 이메일인지 확인하고, 인증코드를 전송한다.
    $('.confirmCode').click(searchIDCode); // 인증코드와 사용자가 입력한 코드가 일치하는지 확인한다.
    $('#checkIdBtn').click(paintID); // 인증코드가 일치한다면 해당 사용자의 ID를 화면에 표시한다.
	passwordInputs.on('keyup', checkPasswordMatch); // 비밀번호와 비밀번호 확인이 일치하는지 확인한다.
	
	function checkPasswordMatch() {
        const currentId = $(this).attr('id');
        let password, confirmPassword;

        if (currentId === 'password' || currentId === 'confirmPassword') {
            password = $('#password').val();
            confirmPassword = $('#confirmPassword').val();
        } else {
            password = $('#newPassword').val();
            confirmPassword = $('#confirmNewPassword').val();
        }

        const message = password !== confirmPassword ? "비밀번호가 일치하지 않습니다." : "비밀번호가 일치합니다.";
        const color = password !== confirmPassword ? "red" : "black";
        doNotMatchPassword.text(message).css("color", color);
    }

    function toggleVisibility(hideElement, showElement) {
        $(hideElement).addClass("hidden");
        $(showElement).removeClass("hidden");
    }

    function memberIdCheck() {
        const id = $('#user_id').val();
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
        const first_name = $('#first_name').val();
        const last_name = $('#last_name').val();
        const birth_date = new Date($('#birth_date').val());
        const currentYear = new Date().getFullYear();
        
        if (user_id.length < 4 || user_id.length > 10) {
            alert('ID는 3 ~ 10자로 입력해주세요.');
            return false;
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

        if (doNotMatchPassword.text() === "비밀번호가 일치합니다.") {
            return true;
        }
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

	function gotoResetPasswordPage() {
		const resultMessage = $(".resultMessage3");
		
		if (resultMessage.text() === "코드가 일치합니다.") {
			$('#resetPassword').removeClass('hidden')
			$('#paintSearchPW').addClass('hidden')
		} else {
			alert('코드를 확인해주세요.')
		} 
	}
	
	function resetPassword() {
		const newPassword = $('#newPassword').val();
		const confirmNewPassword = $('#confirmNewPassword').val();
		const id = $('#idForPassword').val();
		
        if(newPassword === confirmNewPassword) {
			$.ajax({
				url: 'resetPassword',
				type: 'post',
				data: {password: newPassword, user_id: id},
				dataType: 'json',
				success: function(successReset) {
					if(successReset) {
					    alert('비밀번호가 정상적으로 수정되었습니다.')
					    window.location.href = 'http://localhost:8888/ringo/member/login';
				    } else {
						alert('알 수 없는 이유로 수정이 거부되었습니다.')
					}
				},
				error: function() {
				    alert('알 수 없는 이유로 수정이 거부되었습니다.')
				}
			});
		} else {
			alert('비밀번호가 일치하지 않습니다.')
			return false;
		}
       
	}
});