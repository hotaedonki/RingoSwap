let wordList = [];      //게임용 단어목록 저장을 위한 전역변수 배열
let count = 0;          //게임용 단어목록 순서를 저장하는 전역변수
let answerList =[];     //게임용 각 질문당 정답여부를 기록하는 전역변수 배열

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
						<h1 id="word-print">虎</h1>
						<h1 id="pron-print">[とら]</h1>
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

        dictationQuestionSave();
        $(document).on('click', '#button-addon2', nextQuestionPrint);
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
//단어목록을 전역변수에 저장
function dictationQuestionSave(){
    $.ajax({
        url: 'gameNotePrint',
        type:'post',
        dataType:'json',
        success:function(res){
            console.log(res);
            let setting = res.setting;
            wordList = res.wordList;
            console.log(wordList);
            count = 0;
        
        	dictationQuestionPrint();
        },
        error:function(e){
            console.log(e);
        }
    })
}
//저장한 전역변수를 순서대로 출력
function dictationQuestionPrint(){
    let word = wordList[count];
    console.log(word.word);
    console.log(word.pron);
    console.log(wordList[count].mean);
    $('#word-print').text(word.word);
    $('#pron-print').text(word.pron);
    
    console.log('출력끝');
}

function nextQuestionPrint(){
    let answer = $('.form-control').val();
    console.log(answer);
    let correct = wordList[count].mean;

    if(answer != correct){
        answerList[count] = false;
    }else{
        answerList[count] = true;
    }
    console.log(answerList[count]);

    count++;

    if(count >= wordList.length){
        alert('마지막 문제입니다');
    }else {
        dictationQuestionPrint();
    }
}