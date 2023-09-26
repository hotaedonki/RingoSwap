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

$(document).ready(function() {
	$('.submit-password').on('click', checkPassword);
	$(document).on('click', '#sendEmail', sendEmail);
	$(document).on('input', '#inputNewPassword, #inputCheckNewPassword', passwordCheck);
});