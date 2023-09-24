function playDictation() {
	const dictationHTML = `
		<section class="home container dictation-container">
			<div class="row dictation-option-line">
				<div class="col-3 d-flex flex-row">
					<i class="bi bi-arrow-return-left return-to-game-main" data-bs-toggle="modal" data-bs-target="#confirmModal"></i>
				</div>
				<div class="col-6 currentProblemNumber d-flex justify-content-center">
					1/50</div>
				<div class="col-3 progress timeCount">
					<div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100" style="width: 25%;"></div>
				</div>
			</div>
			<div class="row d-flex justify-content-center">
				<div class="card border-dark dictation-question-box mb-3 mt-5">
					<div class="dictation-mean-area">
						<h1>虎</h1>
						<h1>[とら]</h1>
					</div><br>
					<div class="dictation-answer-area">
						<div class="input-group mb-3">
					      <input type="text" class="form-control" placeholder="Write the answer." aria-label="Write the answer." aria-describedby="button-addon2">
					      <button class="btn btn-primary btn-lg" type="button" id="button-addon2">Submit</button>
					    </div>
					</div>
				</div>
			</div>
		</section>
		`;
		
		$('.main-container').hide();
		$("body").append(dictationHTML);
}

function dictationResultScreen() {
	const dictationResultHTML = `
		<section class="home container dictation-result-container">
			<div>받아 쓰기 결과 화면</div>
		</section>`;
	
	$("body").append(dictationResultHTML);
}