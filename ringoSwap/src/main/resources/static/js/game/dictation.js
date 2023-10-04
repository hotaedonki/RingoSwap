let count = 0;          //게임용 단어목록 순서를 저장하는 전역변수

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
					<span id="currentNum">1</span>/<span id="totalNum">50</span> 
                </div>
				<div class="col-3 progress timeCount">
					<div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100" style="width: 25%;"></div>
				</div>
			</div>
			<div class="row d-flex justify-content-center">
				<div class="card border-dark dictation-question-box mb-3 mt-5">
					<div class="dictation-mean-area">
						<h1 id="word-print"></h1>
						<h1 id="pron-print"></h1>
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

		startProgressBar("dictation");
}

function dictationResultScreen() {
    //정답갯수, 오답갯수를 변수 2개에 정의
    let trueCount = answerList.filter(value => value === true).length;
    let falseCount = answerList.filter(value => value === false).length;
	let score = (trueCount / answerList.length) * 100;

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
					<div class="dictation-accuracy-rate">${score}%</div>
				</div>
			</div>
			<div class="row dictation-answer-and-wrongAnswer d-flex justify-content-center align-items-center">
				<div class="col-2 ms-5"></div>
				<div class="col-2 blue"><i class="bi bi-circle"></i> 정답: ${trueCount}개</div>
				<div class="col-2 red"><i class="bi bi-x"></i> 오답: ${falseCount}개</div>
				<div class="col-4 dictation-retry ms-5">
					<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#retryModal">Retry Incorrect</button>
				</div>
			</div>
			<div class="row d-flex justify-content-center h-75">
				<div class="card border-dark dictation-result-box mb-3 mt-3 w-75">
					<div class="row card-body vertical-line">
					</div>
				</div>
			</div>
		</section>
		`;
	
    
	$("body").append(dictationResultHTML);
	$('.dictation-container').remove();
    dictationAnswerPrint();
}
//단어목록을 전역변수에 저장
function dictationQuestionSave(){
    console.log('왜 안됨?');
    $.ajax({
        url: 'gameNotePrint',
        type:'post',
        data:{category : 'dictation'},
        dataType:'json',
        success:function(res){
            console.log('왜 안됨?');
            console.log(res.setting.file_num);
            if(res.setting.file_num === -1){
                //file_num이 -1 == 설정되지 않았을경우 null값을 리턴하므로, 알림창 출력 후  gameMain페이지로 리턴한다.
                alert('게임을 실행하기 전 먼저 메인창에서 게임에 사용할 단어장을 설정해주세요.');
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
			if(res.setting.file_num === -10){       //오답노트인지 여부를 체크
				wrongType = true;
			}
            wordList = res.wordList;
            console.log(wordList);
            count = 0;      //count변수 초기화
			fileNum = res.setting.file_num;	//파일번호 설정
            $('#totalNum').text(res.wordList.length);      //문제 갯수 총합 출력
        
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
    $('#currentNum').text(count + 1);		//현재 문제순번 출력

    if(printSet ==='title'){
        $('#word-print').text(word.word);
    }else{
        $('#word-print').text(word.mean);
    }
    if(pronShow){
        $('#pron-print').text(word.pron);
    }else{
        $('#pron-print').hide();
    }
    
    console.log('출력끝');
}
//정답여부를 계산한 후, 다음문제를 출력하기위한 연산을 실행하는 함수
function nextQuestionPrint(){
    let answer = $('.form-control').val();
    console.log(answer);
    let correct
    if(printSet === 'title'){
        correct = wordList[count].mean;
    }else {
        correct = wordList[count].word;
    }

    if(answer != correct){
        answerList[count] = false;
    }else{
        answerList[count] = true;
    }
    console.log(count+'번째', answerList[count]);
    $('.form-control').val('');
    count++;

    if(count >= wordList.length){
        alert('마지막 문제입니다');
        clearInterval(interval);
        dictationResultScreen();
    }else {
        dictationQuestionPrint();
        startProgressBar();
    }
}
function dictationAnswerPrint() {
    let cnt = 0;
    let rightWord = [];
    let wrongWord = [];

    wordList.forEach(word => {
        let questionHTML = `
            <div class="col-6 mb-3">
                <div class="question-block">
                    <h5><i class="bi bi-pen"></i> Question ${cnt + 1}</h5>
                    <span class="dictation-result-word">단어 : ${word.word} | </span>
                    <span class="dictation-result-pronunciation"> 발음 : ${word.pron} | </span>
                    <span class="dictation-result-mean"> 정답 : ${word.mean}</span>
                </div>
            </div>
        `;

        let answerHTML = `
            <div class="col-6 mb-3">
                <div class="answer-block">
                    <h5>Answer ${cnt + 1}</h5>
                    <span>정답 : ${answerList[cnt].answer}</span>
                    ${answerList[cnt].kotae ? '<i class="bi bi-circle blue"></i>' : '<i class="bi bi-x red"></i>'}
                </div>
            </div>
        `;

        $('.vertical-line').append(questionHTML + answerHTML);
        
        if (answerList[cnt].kotae) {
            rightWord.push(wordList[cnt]);
        } else {
            wrongWord.push(wordList[cnt]);
        }

        cnt++;
    });

    if (!pronShow) {
        $('.dictation-result-pronunciation').hide();
    }
    console.log(wrongType);
    if (wrongType) {
        console.log(rightWord);
        wrongDelete(rightWord);
    } else {
        console.log(JSON.stringify({ wrongWordList: wrongWord }));
        wrongInsert(wrongWord);
    }

    let score = (rightWord.length / wordList.length) * 100;
    let Gcategory = "dictation"; // 수정: mcq에서 dictation으로 변경
    let rightLength = rightWord.length;
    let gameLength = wordList.length;
    console.log("정답률 : " + score);
    gameLogInsert(score, Gcategory, fileNum, rightLength, gameLength);
}
