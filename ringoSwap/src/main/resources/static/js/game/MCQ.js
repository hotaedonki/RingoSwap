let index = 0;		//전역변수
let answer = null;	//각 객관식 문제의 정답을 저장하는 전역변수.
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
					<span id="currentNum">1</span>/<span id="totalNum">50</span> 
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
								<button type="button" class="btn btn-outline-primary answer1">1번답</button>
							</div>
							<div>
								<button type="button" class="btn btn-outline-primary answer2">2번답</button>
							</div>
							<div>
								<button type="button" class="btn btn-outline-primary answer3">3번답</button>
							</div>
							<div>
								<button type="button" class="btn btn-outline-primary answer4">4번답</button>
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
	gameQuestionSave();
	
}

function MCQResultScreen() {
    //정답갯수, 오답갯수를 변수 2개에 정의
    let trueCount = answerList.filter(value => value === true).length;
    let falseCount = answerList.filter(value => value === false).length;

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
				<div class="col-2 blue"><i class="bi bi-circle"></i> 정답: ${trueCount}개</div>
				<div class="col-2 red"><i class="bi bi-x"></i> 오답: ${falseCount}개</div>
				<div class="col-4 MCQ-retry ms-5">
					<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#retryModal">Retry Incorrect</button>
				</div>
			</div>
			<div class="row d-flex justify-content-center h-75">
				<div class="card border-dark MCQ-result-box mb-3 mt-3 w-75">
					<div class="row card-body vertical-line">
					</div>
				</div>
			</div>
		</section>
		`;
		
	$("body").append(MCQResultHTML);
	$('.MCQ-container').remove();
	gameAnswerPrint();
}

function gameQuestionSave(){
	$.ajax({
        url: 'gameNotePrint',
        type:'post',
        data:{category : 'MCQ'},
        dataType:'json',
        success:function(res){
            console.log('MCQ desu');
            console.log(res.setting.file_num);
            if(res.setting.file_num === -1){
                //file_num이 -1 == 설정되지 않았을경우 null값을 리턴하므로, 알림창 출력 후  gameMain페이지로 리턴한다.
                alert('게임을 실행하기 전 먼저 메인창에서 게임에 사용할 단어장을 설정해주세요.');
                returnToGameMain();
                return;
            }else if(res.wordList.length < 4){
				alert('객관식 게임을 진행하기에는 단어장의 단어가 너무 적습니다. 단어를 추가로 설정해주세요');
                returnToGameMain();
                return;
			}
            let setting = res.setting;
            if(setting.pron_show){
                pronShow = true;
            }
            if(setting.order_type){
                printSet = setting.order_type;
            }
			if(res.setting.file_num === -10){		//오답노트인지 여부를 체크
				wrongType = true;
			}
            wordList = res.wordList;

            console.log(wordList);
            index = 0;      //index변수 초기화
			answer = null;	//answer변수 초기화
			answerList = [];	//변수 초기화
            $('#totalNum').text(res.wordList.length);      //문제 갯수 총합 출력
        
        	gameQuestionPrint();
        },
        error:function(e){
            console.log(e);
        }
    })
}
function gameQuestionPrint(){
	let word = wordList[index];
	$('#currentNum').text(index + 1);
	if(printSet == 'title'){
		$('.MCQ-word').text(word.word);
		$('.MCQ-pronunciation').text(word.pron);
		answer = word.mean;
	}else {
		$('.MCQ-word').text(word.mean);
		$('.MCQ-pronunciation').hide();
		answer = word.word;
	}
	console.log(answer);
	loadQuestion(answer);
}

function loadQuestion(answer) {
    $.ajax({
        url: 'MCQShufflePrint',
        type: 'POST',
		contentType:'application/json; charset=utf-8',
		data: JSON.stringify({wordList : wordList
				, index: index
				, correctAnswer : answer
				, formType: printSet}),
		dataType: 'json',
        success: function(options) {
            // 선택지 출력
            $('.answer1').text(options[0]);
            $('.answer2').text(options[1]);
            $('.answer3').text(options[2]);
            $('.answer4').text(options[3]);
			console.log('문제출력');
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function chosenAnswer() {
	let chosenAnswer = $(this).text();
	console.log(chosenAnswer);
	checkAnswer(chosenAnswer);
}

function checkAnswer(chosenAnswer) {
	if(chosenAnswer === answer){
		answerList[index] = {kotae:true, answer: chosenAnswer};
	}else {
		answerList[index] = {kotae:false, answer: chosenAnswer};
	}
	console.log(answerList[index]);
	if(index >= wordList.length){
		// 모든 문제를 완료한 경우 결과 화면 표시
		alert('모든 문제를 풀었습니다.');
		MCQResultScreen();
	}else {
		// 다음 문제 로드
		gameQuestionPrint();
	}
	index++;	//index 카운터 증가
}
function gameAnswerPrint(){
    let cnt = 0;
    wordList.forEach(word => {
        $('.vertical-line').append(`
            <div class="col-12 show-MCQ-result-word" id="answer${cnt}" style="width : 50%">
                <i class="bi bi-pen></i>
                <span class="MCQ-result-word">${word.word} | </span>
                <span class="MCQ-result-pronunciation">${word.pron} | </span>
                <span class="MCQ-result-mean">${word.mean}</span>
            </div>
        `);
		$(`#answer${cnt}`).append(`
			<span class="MCQ-answer">정답 : ${answerList[cnt].answer}</span>
		`);
        if(answerList[cnt].kotae){
            $(`#answer${cnt}`).append(`
                <i class="bi bi-circle blue"></i>
            `);
        }else{
            $(`#answer${cnt}`).append(`
                <i class="bi bi-x red"></i>
            `);
        }
        cnt++
    });
    if(!pronShow){
        $('.dictation-result-pronunciation').hide();
    }

}