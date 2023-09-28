function checkPasswordMatch() {
	const newPassword = $('#password, #newPassword').val();
	const confirmPassword = $('#confirmPassword, #confirmNewPassword').val();
	
	if(newPassword === confirmPassword && newPassword !== '') {
		$('#confirmPassword, #confirmNewPassword').addClass('is-valid').removeClass('is-invalid');
	} else {
		$('#confirmPassword, #confirmNewPassword').addClass('is-invalid').removeClass('is-valid');
	}
}

function memberIdCheck() {
    const user_id = $('#user_id').val();
    console.log(user_id.length);
    
    $('.id-length-check-modal').remove();
    
    if(user_id.length < 4 || user_id.length > 10) {
		$("body").append(`
			<div class="modal show id-length-check-modal" tabindex="-1">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">ID 확인</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <p>ID는 4~10자 사이여야 합니다.</p>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-bs-dismiss="modal">확인</button>
                  </div>
                </div>
              </div>
            </div>
		`)
		$('.id-length-check-modal').modal('show');
		return;
	}
	
	$.ajax({
        url: 'idCheck',
        type: 'post',
        data: { user_id: user_id },
        success: function(result) {
            if(result) {
                showModal("ID 확인", `${user_id}은(는) 이미 사용중입니다.`, ["다시 입력"]);
            } else {
                showModal("ID 확인", "이 ID는 사용 가능합니다. 사용하시겠습니까?", ["다시 입력", "사용"], function(choice) {
                    if(choice === "사용") {
                        $("#user_id").val(user_id);
                    }
                });
            }
        }
    });
}

function showModal(title, message, buttons, callback) {
	$('.id-check-modal').remove();
	
    $("body").append(`
        <div class="modal show id-check-modal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">${title}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p>${message}</p>
                    </div>
                    <div class="modal-footer">
                        ${buttons.map(btn => `<button type="button" class="btn btn-primary modal-btn" data-choice="${btn}">${btn}</button>`).join('')}
                    </div>
                </div>
            </div>
        </div>
    `);
    $('.id-check-modal').modal('show');

    $(".modal-btn").on("click", function() {
        const chosen = $(this).attr("data-choice");
        $('.modal').modal('hide');
        $('.modal').remove();
        if(callback) callback(chosen);
    });
}


function isValidEmail(email) {
    const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    return regex.test(email);
}

function UsedEmail(email) {
	let isUsed = false;
	
	$.ajax({
		url: 'emailCheck'
		, type: 'post'
		, data: { email: email }
		, async: false
		, success: function(response) {
			isUsed = response;
		}
	})
	return isUsed;
}

function emailConfirm(event) {
    const btn = event.target;
    const dataId = btn.getAttribute('data-id');
    let email = $('#email' + dataId).val();
    if (!isValidEmail(email)) {
        alert('유효하지 않은 이메일 주소입니다.');
        return; // 종료
    }
    
    if(UsedEmail(email)) {
		alert('이미 사용중인 이메일 주소입니다.');
		return;
	}

    alert(`${email}로 인증코드를 전송하였습니다.`);
	
    $.ajax({
        url: 'emailConfirm',
        type: 'post',
        data: { email: email },
        success: function() {
            
        }
    });
}

function emailConfirmForSearchID(event) {
	const btn = event.target;
    const dataId = btn.getAttribute('data-id');
    let email = $('#email' + dataId).val();
    
    if (!isValidEmail(email)) {
        alert('유효하지 않은 이메일 주소입니다.');
        return; // 종료
    }
    
    if(!UsedEmail(email)) {
		alert('존재하지 않는 이메일 주소입니다.');
		return;
	}

    alert(`${email}로 인증코드를 전송하였습니다.`);
	
    $.ajax({
        url: 'emailConfirm',
        type: 'post',
        data: { email: email },
        success: function() {
            
        }
    });
}


function searchIDCode(event) {
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
                    resultMessage.text("The code matches.").css("color", "black");
                    break;
                case 'INCORRECT':
                case 'CHECKINPUT':
                    resultMessage.text("The code does not match.").css("color", "red");
                    break;
                default:
                    resultMessage.text("An unknown error occurred.").css("color", "red");
                    break;
            }
        }
    });
}

function paintID() {
	const email = $('#email2').val();
    const resultMessage = $(".resultMessage2");
    const paintIDElement = $(".paintID");
    
    if (resultMessage.text() === "The code matches.") {
    
	    $.ajax({
			url:'userIDByEmail'
			, type: 'post'
			, data: { email: email}
			, success: function(user_id) {
				 paintIDElement.text(`이메일에 해당하는 ID는 ${user_id} 입니다.`).css("color", "black");
			}		
		})
	} else {
		alert('코드를 먼저 올바르게 입력하세요.')
	}   
}

function submitSignUp() {
    if (joinCheck()) { 
        submitSignUpForm();
    }
}

function nicknameCheck(nickname) {
	let isUsed = false;
	
	$.ajax({
		url: 'nicknameCheck'
		, type: 'post'
		, data: { nickname: nickname }
		, success: function(response) {
			isUsed = response;
		}
	})
	return isUsed;
}

function joinCheck() {
    const user_id = $('#user_id').val();
    const password = $('#password').val();
    const first_name = $('#first_name').val();
    const last_name = $('#last_name').val();
    const nickname = $('#username').val(); // Nickname
    const gender = $('#gender').val();
    const birth_date = new Date($('#birth_date').val());
    const currentYear = new Date().getFullYear();
    const email = $('#email1').val();
    const native_lang = $('#native_lang').val();
    const target_lang = $('#target_lang').val();
    const checkEmail = $('#resultMessage1').text();
    
    if (user_id.length < 4 || user_id.length > 10) {
        alert('ID는 3 ~ 10자로 입력해주세요.');
        return false;
    }
    
    if (password.length < 4 || password.length > 10)
    {
		alert('비밀번호의 길이는 3 ~ 10자 입니다.');
		return false;
	}
	
	if ($('#confirmPassword').hasClass('is-invalid')) 
	{
		alert('비밀번호가 일치하지 않습니다.');
        return false;
    }

    if (first_name === '') {
        alert('성을 입력해주세요.');
        return false;
    }

    if (last_name === '') {
        alert('이름을 입력해주세요.');
        return false;
    }

	if (nickname === '') {
		alert('닉네임을 입력해주세요.')
		return false;
	}
	
	if(nicknameCheck(nickname)) {
		alert('이미 사용중인 닉네임입니다.')
		return false;
	}
	
	if (gender === null || gender.length <= 0) {
		alert('성별을 선택해주세요.')
		return false;
	}
	
	if (isNaN(birth_date.getTime())) {
	    alert("날짜를 선택해주세요.");
	    return false;
	}
	
    if (currentYear - birth_date.getFullYear() > 110) {
        alert("날짜를 다시 입력해주세요.");
        return false;
    }
    
    if (email.length <= 0) {
		alert("이메일을 입력해주세요.");
		return false;
	}
	
	if (checkEmail === "The code does not match."){
		alert("코드가 일치하지 않습니다.")
		return false;
	}

	if (native_lang === null || native_lang.length <= 0) {
		alert("모국어를 선택해주세요.");
		return false;
	}
	
	if (target_lang === null || target_lang.length <= 0) {
		alert("배우고 싶은 언어를 선택해주세요.")
		return false;
	}
	
	if (native_lang === target_lang) {
	    alert("모국어와 배우고 싶은 언어는 동일할 수 없습니다.");
	    return false;
	}

    return true;
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
	console.log("코드일치?", resultMessage)
	
	if (resultMessage.text() === "The code matches.") {
		showResetPassword();
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

function getSignUpData() {
    return {
        user_id: $('#user_id').val(),
        password: $('#password').val(),
        confirmPassword: $('#confirmPassword').val(),
        first_name: $('#first_name').val(),
        last_name: $('#last_name').val(),
        nickname: $('#username').val(),
        gender: $('#gender').val(),
        birth_date: $('#birth_date').val(),
        email: $('#email1').val(),
        verificationCode: $('#code1').val(),
        native_lang: $('#native_lang').val(),
        target_lang: $('#target_lang').val(),
        introduction: $('#introduction').val()
    };
}

function submitSignUpForm() {
    const data = getSignUpData();
	console.log(data);
    $.ajax({
        type: 'POST',
        url: 'join',
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function(response) {
			alert('가입되었습니다!');
            window.location.href = "../member/login";
        },
        error: function(error) {
            console.error('Error during sign up:', error);
        }
    });
}

function returnToHome() {
	window.location.href = '../member/login';
}

/*
function checkSession() {
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

// 세션 클리어
function removeAllSessionJoin()
{
	$.ajax({
		url: 'removeAllSessionJoin'
		, type:'post'
		, success: function()
		{
			console.log("세션 제거 완료");
		}
		, error: function(e)
		{
			alert(e);
		}
	});
}*/
