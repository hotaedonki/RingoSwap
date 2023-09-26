function playMCQ() {
    //history API기능을 위한 url변수
    const currentUrl = window.location.href;
    const newUrl = 'http://localhost:8888/ringo/game/gameMain?category=MCQ';
	const MCQAreaHTML = `
		<section class="home container MCQ-container">
			<div class="row MCQ-option-line">
				<div class="col-3 d-flex flex-row">
					<i class="bi bi-arrow-return-left" data-bs-toggle="modal" data-bs-target="#confirmModal"></i>
				</div>
				<div class="col-6 currentProblemNumber d-flex justify-content-center">
					1/50
				</div>
				<div class="col-3 progress timeCount">
					<div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100" style="width: 25%;"></div>
				</div>
			</div>
			<div class="row d-flex justify-content-center">
				<div class="card border-dark MCQ-question-box mb-3 mt-5">
					<div class="row card-body">
						<div class="col-7 show-MCQ-word d-flex flex-column justify-content-center align-items-center">
							<h1 class="MCQ-word">단어</h1>
							<h2 class="MCQ-pronunciation">[발음]</h2>
						</div>
						<div class="col-5 MCQ-answer-box d-flex flex-column justify-content-center align-items-end">
							<div>
								<button type="button" class="btn btn-outline-primary answer-1">1번답</button>
							</div>
							<div>
								<button type="button" class="btn btn-outline-primary answer-2">2번답</button>
							</div>
							<div>
								<button type="button" class="btn btn-outline-primary answer-3">3번답</button>
							</div>
							<div>
								<button type="button" class="btn btn-outline-primary answer-4">4번답</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>`;
	$('.main-container').hide();
	$("body").append(MCQAreaHTML);
	if(currentUrl !== newUrl){
		history.pushState({ category:'MCQ', url: newUrl }, '', `?category=MCQ`);
	}
	loadQuestion();
	
}

function MCQResultScreen() {
	const MCQResultHTML = `
		<section class="home container MCQ-result-container">
			<div class="row MCQ-result-option-line">
				<div class="col-3 d-flex flex-row">
					<i class="bi bi-arrow-return-left" data-bs-toggle="modal" data-bs-target="#returnToMainModal"></i>
				</div>
				<div class="col-6 MCQ-name d-flex justify-content-center">
					Multiple Choice Quiz
				</div>
				<div class="col-3 d-flex align-items-center flex-column">
					<div>Accuracy</div>
					<div class="MCQ-accuracy-rate">100%</div>
				</div>
			</div>
			<div class="row MCQ-answer-and-wrongAnswer d-flex justify-content-center align-items-center">
				<div class="col-2 ms-5"></div>
				<div class="col-2 blue"><i class="bi bi-circle"></i> 정답: 4개</div>
				<div class="col-2 red"><i class="bi bi-x"></i> 오답: 5개</div>
				<div class="col-4 MCQ-retry ms-5">
					<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#retryModal">Retry Incorrect</button>
				</div>
			</div>
			<div class="row d-flex justify-content-center h-75">
				<div class="card border-dark MCQ-result-box mb-3 mt-3 w-75">
					<div class="row card-body vertical-line">
						<div class="col-12 show-MCQ-result-word">
							<h1 class="MCQ-result-word">rion</h1>
							<h2 class="MCQ-result-mean">토끼</h2>
							<i class="bi bi-circle blue"></i><i class="bi bi-x red"></i>
						</div>					
					</div>
				</div>
			</div>
		</section>
		`;
		
	$("body").append(MCQResultHTML);
}

function loadQuestion() {
    $.ajax({
        url: 'gameNotePrint',
        type: 'POST',
        success: function(response) {
            // 단어와 발음 출력
            $('.MCQ-word').text(response.selectedWord.word);
            // 발음이 있다면 출력 (이 부분은 단어 객체에 발음 정보가 있다고 가정하고 작성되었습니다)
            if (response.selectedWord.pronunciation) {
                $('.MCQ-pronunciation').text('[' + response.selectedWord.pronunciation + ']');
            } else {
                $('.MCQ-pronunciation').text('');
            }

            // 선택지 출력
            $('.answer-1').text(response.options[0]);
            $('.answer-2').text(response.options[1]);
            $('.answer-3').text(response.options[2]);
            $('.answer-4').text(response.options[3]);
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function chosenAnswer() {
	let chosenAnswer = $(this).text();
	checkAnswer(chosenAnswer);
}

function checkAnswer(chosenAnswer) {
	$.ajax({
        url: 'checkMCQAnswer', 
        type: 'POST',
        data: {
            answer: chosenAnswer
        },
        success: function(response) {
            if (response.isCorrect) {
				//정답
            } else {
                // 오답
            }

            updateProgress();

            // 모든 문제 완료 확인
            let current = parseInt($('.currentProblemNumber').text().split('/')[0]);
            let total = parseInt($('.currentProblemNumber').text().split('/')[1]);
            if (current >= total) {
                // 모든 문제를 완료한 경우 결과 화면 표시
                MCQResultScreen();
            } else {
                // 다음 문제 로드
                loadQuestion();
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function updateProgress() {
    let current = parseInt($('.currentProblemNumber').text().split('/')[0]);
    let total = parseInt($('.currentProblemNumber').text().split('/')[1]);

    current++;

    $('.currentProblemNumber').text(current + "/" + total);

    let progressPercentage = (current / total) * 100;
    $('.progress-bar').css('width', progressPercentage + '%').attr('aria-valuenow', progressPercentage);
}