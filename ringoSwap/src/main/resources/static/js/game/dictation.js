function playDictation() {
    //history API기능을 위한 url변수
    const currentUrl = window.location.href;
    const newUrl = 'http://localhost:8888/ringo/game/gameMain?category=dictation';

	const dictationHTML = `
		<section class="home container dictation-container">
			<div class="row dictation-option-line">
				<div class="col-3 d-flex flex-row">
					<i class="bi bi-arrow-return-left" data-bs-toggle="modal" data-bs-target="#confirmModal"></i>
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
        if(currentUrl !== newUrl){
            history.pushState({ category:'dictation', url: newUrl }, '', `?category=dictation`);
        }
}

function dictationResultScreen() {
	const dictationResultHTML = `
		<section class="home container dictation-result-container">
			<div class="row dictation-result-option-line">
				<div class="col-3 d-flex flex-row">
					<i class="bi bi-arrow-return-left" data-bs-toggle="modal" data-bs-target="#returnToMainModal"></i>
				</div>
				<div class="col-6 dictation-name d-flex justify-content-center">
					Dictation
				</div>
				<div class="col-3 d-flex align-items-center flex-column">
					<div>Accuracy</div>
					<div class="dictation-accuracy-rate">100%</div>
				</div>
			</div>
			<div class="row dictation-answer-and-wrongAnswer d-flex justify-content-center align-items-center">
				<div class="col-2 ms-5"></div>
				<div class="col-2 blue"><i class="bi bi-circle"></i> 정답: 4개</div>
				<div class="col-2 red"><i class="bi bi-x"></i> 오답: 5개</div>
				<div class="col-4 dictation-retry ms-5">
					<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#retryModal">Retry Incorrect</button>
				</div>
			</div>
			<div class="row d-flex justify-content-center h-75">
				<div class="card border-dark dictation-result-box mb-3 mt-3 w-75">
					<div class="row card-body vertical-line">
						<div class="col-12 show-dictation-result-word">
							<h1 class="dictation-result-word">rion</h1>
							<h2 class="dictation-result-pronunciation">라이온</h1>
							<h2 class="dictation-result-mean">토끼</h2>
							<i class="bi bi-circle blue"></i><i class="bi bi-x red"></i>
						</div>					
					</div>
				</div>
			</div>
		</section>
		`;
	
	$("body").append(dictationResultHTML);
}