function checkPassword() {
	const password = $('#InputPassword1').val();
	console.log(password);
	$.ajax({
		url: 'checkPassword'
		, type: 'post'
		, data: {password: password}
		, success: function(res) {
			if(res.success) {
				printPersonalInfoPage(res);
			} else {
				alert("패스워드가 일치하지 않습니다.")
			}
		},
		error: function(error) {
			console.log(error);
		}
	});
}

function printPersonalInfoPage(res) {
	$('.check-password-card').hide();
	
	const PersonalInfoPage = `
		<div class="card border-primary mb-3 personal-information-box">
			<div class="card-header">Personal Information</div>
			<div class="card-body personal-card pt-1">
				<div class="form-group">
					<fieldset>
						<label class="form-label mt-4" for="readOnlyInput">ID </label> <input
							class="form-control ID" id="readOnlyInput" type="text"
							placeholder="ID" readonly="">
					</fieldset>
				</div>
				<div class="form-group has-success">
					<label class="form-label mt-4" for="inputValid">Password</label> <input
						type="text" value="Password"
						class="form-control is-valid password" id="inputValid">
					<div class="valid-feedback">password</div>
				</div>
				<div class="form-group has-danger">
					<label class="form-label mt-4" for="inputInvalid">Password Check
						</label> <input type="text" value="Password Check"
						class="form-control is-invalid password-check" id="inputInvalid">
					<div class="invalid-feedback">not correct</div>
				</div>
				<div class="form-group">
					<div class="input-group mb-3 mt-4">
				      <input type="text" class="form-control" placeholder="Email" aria-label="Email" aria-describedby="button-addon2">
				      <button class="btn btn-primary" type="button" id="sendEmail">Button</button>
				    </div>
				    <div class="form-group has-success">
					  <label class="form-label mt-1" for="inputValid">코드 체크하는 칸 기본 hide임</label>
					  <input type="text" value="correct value" class="form-control is-valid" id="inputValid">
					  <div class="valid-feedback"></div>
					</div>
				</div>
				<div class="form-group">
					<label for="exampleSelect1" class="form-label mt-4">성별 변경
						</label> <select class="form-select" id="exampleSelect1">
						<option>여자</option>
						<option>남자</option>
						<option>기타</option>
					</select>
				</div>
				<fieldset class="form-group">
					<legend class="mt-4">배우고 싶은 언어 고르기</legend>
					<div class="form-check disabled">
						<input class="form-check-input" type="radio" name="optionsRadios"
							id="optionsRadios3" value="option3" disabled=""> <label
							class="form-check-label" for="optionsRadios3"> 모국어인 경우 </label>
					</div>
					<div class="form-check">
						<input class="form-check-input" type="radio" name="optionsRadios"
							id="optionsRadios1" value="option1" checked=""> <label
							class="form-check-label" for="optionsRadios1"> 일본어 </label>
					</div>
					<div class="form-check">
						<input class="form-check-input" type="radio" name="optionsRadios"
							id="optionsRadios2" value="option2"> <label
							class="form-check-label" for="optionsRadios2"> 영어 </label>
					</div>
				</fieldset>
				<fieldset class="form-group mb-5">
					<legend class="mt-4">팔로워, 팔로잉 공개</legend>
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox"
							id="flexSwitchCheckDefault"> <label
							class="form-check-label" for="flexSwitchCheckDefault">Default
							switch checkbox input</label>
					</div>
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox"
							id="flexSwitchCheckChecked" checked=""> <label
							class="form-check-label" for="flexSwitchCheckChecked">Checked
							switch checkbox input</label>
					</div>
				</fieldset>
				<br>
				<button type="button" class="btn btn-primary submit-info">Primary</button>
			</div>
		</div>`;
		
		$('.container').append(PersonalInfoPage);
}


$(document).ready(function() {
	$('.submit-password').on('click', checkPassword);
});