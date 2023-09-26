let emailChanged = false;
 
function checkNativeLang(res) {
	switch(res.data.native_lang) {
        case 'ko':
            $('.form-check-label.ko').prev().prop('disabled', true);
            break;
        case 'ja':
            $('.form-check-label.ja').prev().prop('disabled', true);
            break;
        case 'en':
            $('.form-check-label.en').prev().prop('disabled', true);
            break;
    }
}

function sendEmail() {
	emailChanged = true;
	$('.check-email-code').show();
}

function passwordCheck() {
	const newPassword = $('#inputNewPassword').val();
	const confirmPassword = $('#inputCheckNewPassword').val();
	
	if(newPassword === confirmPassword && newPassword !== '') {
		$('#inputCheckNewPassword').addClass('is-valid').removeClass('is-invalid');
	} else {
		$('#inputCheckNewPassword').addClass('is-invalid').removeClass('is-valid');
	}
}

function updateInfo() {
    const nickname = $('#nicknameInput').val();
    const password = $('#inputNewPassword').val();
    const checkPassword = $('#inputCheckNewPassword').val();
    const emailCode = $('#inputValid').val();

    if (!nickname) {
        alert('Nickname cannot be empty.');
        return false;
    }

    if (password) {
        if (password !== checkPassword) {
            alert('Passwords do not match.');
            return false;
        }

        if (password.length < 4 || password.length > 10) {
            alert('Password should be between 4 and 10 characters.');
            return false;
        }
    }

    if ($('#sendEmail').hasClass('clicked') && emailCode !== 'correct value') {
        alert('Email verification code does not match.');
        return false;
    }

    if (!confirm('Do you want to update your personal information?')) {
        return false;
    }

    const dataToSend = {
	    nickname: nickname,
	    password: password ? password : null,
	    gender: $('#exampleSelect1').val(),
	    target_lang: $('input[name="optionsRadios"]:checked').closest('.form-check').find('label').text().trim(),
	    trans_lang: $('input[name="optionsRadios"]:checked').closest('.form-check-translang').find('label').text().trim(),
	    reveal_follow: $('#flexSwitchCheckDefault').is(':checked') ? 'public' : 'private'
	};
	
	if (emailChanged) {
	    dataToSend.email = $('input[placeholder="Email"]').val();
	}


    $.ajax({
        url: 'updatePersonalInfo',
        type: 'post',
        data: dataToSend,
        success: function(res) {
            alert("업데이트 성공!!")
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function revealFollow() {
	if ($('#flexSwitchCheckDefault').is(':checked')) {
        $('label[for="flexSwitchCheckDefault"]').text('Public');
    } else {
        $('label[for="flexSwitchCheckDefault"]').text('Private');
    }
}

$(document).ready(function() {
	$('.submit-password').on('click', checkPassword);
	$(document).on('click', '#sendEmail', sendEmail);
	$(document).on('input', '#inputNewPassword, #inputCheckNewPassword', passwordCheck);
	$(document).on('click', '.submit-info', updateInfo);
	$(document).on('change', '#flexSwitchCheckDefault', revealFollow);
});