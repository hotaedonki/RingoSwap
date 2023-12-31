//게임기능에 활용하는 전역변수 목록
let wordList = [];      //게임용 단어목록 저장을 위한 전역변수 배열
let answerList =[];     //게임용 각 질문당 정답여부를 기록하는 전역변수 배열
let answer = null;	//각 객관식 문제의 정답을 저장하는 전역변수.
let printSet = 'title';   //게임용 질문의 형식을 지정하는 전역변수
let pronShow = false;    //게임용 발음부를 보일지 여부를 지정하는 전역변수
let wrongType = false;   //게임용 오답노트인지를 체크하는 전역변수
let fileNum = 0;         //게임용 사용 단어장의 파일번호를 저장하는 전역변수

function confirmExit() {	
	const currentGame = getCurrentGame();
	$('#confirmModal').modal('hide');
	console.log(currentGame);
		if(currentGame === 'MCQ' || currentGame === 'dictation') {
			$(`.${currentGame}-container`).remove();
			window[`${currentGame}ResultScreen`]();
		} else if (currentGame === 'flashCards') {
			$('.flashCards-container').remove();
			$('.main-container').show();
            history.pushState({ url: 'http://localhost:8888/ringo/game/gameMain' }, '', `?category=dictation`);
            console.log(history);
		}

}

function getCurrentGame() {
    const category = getUrlParam('category');
    if ($('.MCQ-container').length > 0) return 'MCQ';
    if ($('.dictation-container').length > 0) return 'dictation';
    if ($('.flashCards-container').length > 0) return 'flashCards';
    if ($('.MCQ-result-container').length > 0) return 'MCQ-result';
    if ($('.dictation-result-container').length > 0) return 'dictation-result';
    return null;
}


function selectAll() {
    let allCheckboxes = $('#wordlistModal input[type="checkbox"]');
    let checkedCheckboxesCount = allCheckboxes.filter(":checked").length;
    let totalCheckboxesCount = allCheckboxes.length;
	
	console.log(allCheckboxes,checkedCheckboxesCount,totalCheckboxesCount)
	
    if (checkedCheckboxesCount <= totalCheckboxesCount / 2) {
        // 체크된 체크박스가 절반 이하라면 전체 체크
        allCheckboxes.prop('checked', true);
    } else {
        // 체크된 체크박스가 절반 초과라면 전체 해제
        allCheckboxes.prop('checked', false);
    }
}

function returnToGameMain() {
    const newUrl = 'http://localhost:8888/ringo/game/gameMain';
	const resultGame = getCurrentGame();
	console.log(resultGame + "-container");
	$(`.${resultGame}-container`).remove();
	
	$('#returnToMainModal').modal('hide');
	$('.main-container').show();
    history.pushState({ url: newUrl }, '', newUrl);
}

function printGame(currentGame){
	console.log(currentGame);
    if (currentGame === "flashcard") {
      // category가 flashcard일 경우 플래시카드 게임 실행
        console.log('flashcard 열기');
        playFlashCards();
        
    }else if(currentGame === "dictation"){
        console.log('dictation 열기');
		playDictation();
	}else if(currentGame === "MCQ"){
        console.log('MCQ 열기');
		playMCQ();
	}else{
		console.log('메인화면인가?');
		if(!currentGame){
			returnToGameMain();
		}
	}
}

function gameSettingPrint(){
    $.ajax({
        url:'gameSettingPrint',
        type:'post',
        dataType:'json',
        success:function(setting){
            let number = setting.question_num;
            console.log('문제갯수 : '+number);
            if(setting.question_num === 0){
                number = 'total';
            }
            console.log('매치속성 : '+setting.match_use);
            $('.btn[data-bs-target="#countOfItemsModal"]').text(number);
            $('.btn[data-bs-target="#countOfItemsModal"]').attr('data-question-num', setting.question_num);
            if(setting.match_use){
                $('#flexSwitchCheckDefault').attr('checked', true);
            }
        },
        error:function(e){
            console.log(e);
        }
    })
}

//오답노트에 오답 목록을 집어넣는 기능
function wrongInsert(wrongWord){
    $.ajax({
		url:'wrongWordInsert',
		type:'post',
        contentType: 'application/json;charset=UTF-8',
		data:JSON.stringify(wrongWord),
		success:function(res){
			console.log(res);
		},
        error:function(e){
            console.log(e);
        }
	})
}
//오답노트에 오답 목록을 삭제하는 기능
function wrongDelete(rightWord){
    $.ajax({
		url:'wrongWordDelete',
		type:'post',
        contentType: 'application/json;charset=UTF-8',
		data:JSON.stringify(rightWord),
		success:function(res){
			console.log(res);
		},
        error:function(e){
            console.log(e);
        }
	})
}

function gameLogInsert(score, Gcategory, fileNum, rightLength, gameLength){
    console.log(score);
    console.log(Gcategory);
    console.log(rightLength);
    console.log(gameLength);
    $.ajax({
        url:"gameLogInsert",
        type:"post",
        data:{score : parseFloat(score)
            , game_category : Gcategory
            , file_num : parseInt(fileNum)
            , rightLength : parseInt(rightLength)
            , gameLength : parseInt(gameLength)
        },
        dataType:'json',
        success:function(res){
            console.log(res);
            if(res === 0){
                console.log('삽입실패');
            }else{
                console.log('삽입성공');
            }
        },
        error:function(e){
            console.log(e);
        }
    })
}

function tryIncorrectQuestion(){
    let category = $('.do-retry').data('game-category');
    if(category === 'MCQ'){
        if(wordList.length <=4){
            alert('객관식 게임을 진행하기에는 오답의 갯수가 너무 적습니다.');
            $('.btn-close').click();
            returnToGameMain();
            return;
        }
        playMCQ('play');
    }else {
        if(wordList.length < 1){
            alert('오답이 존재하지 않습니다.');
            $('.btn-close').click();
            returnToGameMain();
            return;
        }
        playDictation('play');
    }
    $('.btn-close').click();
}
let interval; // setInterval을 저장하는 변수

function startProgressBar(game) {
    const progressBarContainer = document.querySelector(".timeCount");
    progressBarContainer.innerHTML = '';  // 기존 프로그레스 바 제거

    // 새로운 프로그레스 바 생성 및 추가
    const progressBarHTML = `
        <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>
    `;
    progressBarContainer.innerHTML = progressBarHTML;

    const progressBar = progressBarContainer.querySelector(".progress-bar");

    clearInterval(interval); // 현재 실행 중인 interval이 있다면 중지

    const totalTime = 10 * 1000;
    const updateInterval = 100;
    const incrementValue = (100 / (totalTime/updateInterval));
    let currentProgress = 0;

    interval = setInterval(() => {
        currentProgress += incrementValue;
        if (currentProgress > 100) {
            currentProgress = 100;
            clearInterval(interval);
        }
        progressBar.setAttribute("aria-valuenow", currentProgress);
        progressBar.style.width = `${currentProgress}%`;
        if (currentProgress >= 100) {
         if(game === "dictation") {
               nextQuestionPrint(); // 강제로 다음 문제로 넘기기
            } else {
            checkAnswer();
         }
        }
        
    }, updateInterval);
}