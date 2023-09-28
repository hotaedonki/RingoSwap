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
    let target = $('input[name="targetLangRadios"]:checked').closest('.form-check').find('label').text().trim();
    let trans = $('input[name="transLangRadios"]:checked').data('trans-lang');
    console.log(target);
    console.log(trans);

    if (nickname.length <3 || nickname.length > 20) {
        if(nickname){
            alert('Nickname should be between 3 and 20 characters.');
            return false;
        }
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
	

    let dataToSend = {
	    nickname: nickname ? nickname : null,
	    password: password ? password : null,
	    gender: $('#exampleSelect1').val(),
	    target_lang: languageTranser(target),
	    trans_lang: trans,
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
            window.location.href = res;
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
	$('#InputPassword1').on('keyup', function(event){
        if(event.which == 13){
            checkPassword();
        }
    });
	$(document).on('click', '#sendEmail', sendEmail);
	$(document).on('input', '#inputNewPassword, #inputCheckNewPassword', passwordCheck);
	$(document).on('click', '.submit-info', updateInfo);
	$(document).on('change', '#flexSwitchCheckDefault', revealFollow);
});

function languageTranser(lang){
    let result = null;
    if(lang === 'Korean'){
        result = 'ko';
    }else if(lang ==='Japenese'){
        result = 'ja';
    }else if(lang === 'English'){
        result = 'en';
    }
    return result;
}