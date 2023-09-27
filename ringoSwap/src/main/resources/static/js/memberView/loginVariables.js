

$(document).ready(function() {   
	usernamePrint();
    $(document).on('click', '#searchID', showSearchIDForm);
	$(document).on('click', '#searchPW', showSearchPWForm);
	$(document).on('click', '.PWcheckbtn', gotoResetPasswordPage);
	$(document).on('click', '.resetPassword', resetPassword);
	$(document).on('click', '#signUp', showSignUpForm);
	$(document).on('click', '.Register_btn', submitSignUp);
	$(document).on('click', '#idCheck_Btn', memberIdCheck);
	$(document).on('click', '.sendCode', emailConfirm);
	$(document).on('click', '.sendCode-searchID', emailConfirmForSearchID);
	$(document).on('click', '.sendCodeForPassword', emailConfirmForPassword);
	$(document).on('click', '.confirmCode', searchIDCode);
	$(document).on('click', '.submit-check-ID', paintID);
	$(document).on('input', '#password, #confirmPassword, #newPassword, #confirmNewPassword', checkPasswordMatch);
	$(document).on('click', '.return-to-home', returnToHome);
	
	const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    if (error) {
        $('#loginErrorModal').modal('show');
    }
});