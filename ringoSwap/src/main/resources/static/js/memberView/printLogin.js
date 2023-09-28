
// usernamePrint 함수 안에서 username을 가져온 후 밑줄 길이를 조절하는 코드 추가


function failureUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    if (error) {
		console.log("아이");
        
        const loginErrorModal = new bootstrap.Modal(document.getElementById('loginErrorModal'));
        loginErrorModal.show();
        
         urlParams.delete('error');
        
        // 현재 URL에서 error 파라미터를 제거
        const newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
        if (urlParams.toString()) {
            newUrl += '?' + urlParams.toString();
        }
        history.replaceState(null, null, newUrl);
        
    }
}

function showSignUpForm() {
	const signUpForm = `
		<div class="container">
				<div class="container_signup_form" id="signUpForm">
					<div class="signUp-form form">
						<h2>Sign Up</h2>
						<div class="input-group mb-3 mt-3">
					      <input type="text" class="form-control form-control-lg" placeholder="ID" name="user_id" id="user_id">
					      <button class="btn btn-info btn-sm w-25" type="button" id="idCheck_Btn">Duplicate Check</button>
					    </div>
						
						<div class="form-group w-100">
						  <input type="password" class="form-control form-control-lg" placeholder="Password" name="password" id="password">
						  <input type="password" placeholder="Confirm Password" class="form-control password-check form-control-lg" name="confirmPassword" id="confirmPassword">
						  <div class="invalid-feedback">not correct</div>		
						  
						  <input type="text" class="form-control form-control-lg" placeholder="First Name" name="first_name" id="first_name">
						  <input type="text" class="form-control form-control-lg" placeholder="Last Name" name="last_name" id="last_name">
						  <input type="text" class="form-control form-control-lg" placeholder="Nickname" name="username" id="username">
						  <select class="form-select" name="gender" id="gender">
					        <option value="F">Female</option>
					        <option value="M">Male</option>
							<option value="N">Other</option>
					      </select>
					      <input type="date" class="form-control form-control-lg" name="birth_date" id="birth_date" />
						</div>
					
						<!-- Email & Verification -->
						<div class="input-group mb-3 mt-2">
					      <input type="email" class="form-control form-control-lg" placeholder="Email" name="email" id="email1">
					      <button class="btn btn-info btn-sm sendCode w-25" type="button" data-id="1">Send Code</button>
					    </div>
					    <div class="input-group mb-3">
					      <input type="text" class="form-control form-control-lg" placeholder="Verification code" name="verificationCode" id="code1">
					      <button class="btn btn-info btn-sm confirmCode w-25" type="button" data-id="1">Check Code</button>
					    </div>
						<div class="resultMessage1"></div>
						<!-- Language Preferences -->
						<label>Please select your native language.</label> 
						<select class="form-select" name="native_lang" id="native_lang">
							<option value="ko">Korean</option>
							<option value="ja">Japanese</option>
							<option value="en">English</option>
						</select> <label>Please select the language you want to learn.</label> 
						<select class="form-select" name="target_lang" id="target_lang">
							<option value="ja">Japanese</option>
							<option value="ko">Korean</option>
							<option value="en">English</option>
						</select>

						<!-- Introduction & Hobby -->
						<textarea class="form-control" placeholder="Introduce" name="introduction" id="introduction" rows="3"></textarea>
						
						<!-- Submit Button -->
						<div class="checkSection">
							<button type="button" class="btn btn-primary btn-lg Register_btn mb-4 mt-4 me-3 ">Register</button>
							<button type="button" class="btn btn-info btn-lg return-to-home">Return</button>
						</div>
					</div>
				</div>
			</div>`
			
			$('.container_signup_form').remove();
			
			$('#loginForm').hide();
			$('body').append(signUpForm);
}

function showSearchIDForm() {
	const searchIDForm = `
		<div class="container">
			<div class="container_search_form">
				<div class="form_search">
					<h1>Find ID</h1>
					   <div class="input-group mb-3 mt-5">
					      <input type="email" class="form-control" placeholder="Email" name="email" id="email2">
					      <button class="btn btn-info sendCode-searchID w-25" type="button" data-id="2">Send Code</button>
					   </div>
					   <div class="input-group mb-3">
					      <input type="text" class="form-control" placeholder="Verification code" name="verificationCode" id="code2">
					      <button class="btn btn-info confirmCode w-25" type="button" data-id="2">Check Code</button>
					   </div>
					<div class="resultMessage2"></div>
					<div class="paintID"></div>
					<br>
					<div class="checkSection">
						<button type="button" class="btn btn-info submit-check-ID">Check ID</button>
						<button type="button" class="btn btn-info return-to-home">Return</button>
					</div>
				</div>
			</div>
		</div>`;
		
		$('#loginForm').hide();
		$('body').append(searchIDForm);
}

function showSearchPWForm() {
	const searchPWForm = `
		<div class="container paintSearchPW">
			<div class="container_search_form">
				<div class="form_search">
					<h1 class="h1">Find Password</h1>
					<div class="form-group mt-5 w-100">
					  <input class="form-control form-control-lg input_IDForPassword" type="text" placeholder="ID" name="user_id" id="idForPassword">
					</div>
					<div class="input-group mb-3">
					      <input type="email" class="form-control form-control-lg" placeholder="Email" name="email" id="emailForPassword">
					      <button class="btn btn-info sendCodeForPassword w-25" type="button" data-id="2">Send Code</button>
					   </div>
				   <div class="input-group mb-3">
				      <input type="text" class="form-control form-control-lg" placeholder="Verification code" name="verificationCode" id="code3">
				      <button class="btn btn-info confirmCode w-25" type="button" data-id="3">Check Code</button>
				   </div>
					<div class="resultMessage3"></div>
					<br>
					<div class="checkSection">
						<button type="button" class="btn btn-info PWcheckbtn">Update PW</button>
						<button type="button" class="btn btn-info return-to-home">Return</button>
					</div>
				</div>
			</div>
		</div>`;
		
		$('#loginForm').hide();
		$('body').append(searchPWForm);
}

function showResetPassword() {
	const resetPassword = `
		<div class="container">
			<div class="container_signup_form container_signup">
				<div class="form_search">
					<h1 class="h1">Update Password</h1>
					<br> <br>
					<div class="newPassword-section">						
						<div class="form-group w-100">
						  <input type="password" class="form-control form-control-lg" placeholder="New Password" name="password" id="newPassword">
						  <input type="password" placeholder="Confirm Password" class="form-control password-check form-control-lg" name="confirmNewPassword" id="confirmNewPassword">
						  <div class="invalid-feedback">not correct</div>
						<button type="button" class="btn btn-info resetPassword">Update</button>
						<button type="button" class="btn btn-info return-to-home">Return</button>
					</div>
				</div>
			</div>
		</div>`
		
		$('.paintSearchPW').hide();
		$('body').append(resetPassword);
}