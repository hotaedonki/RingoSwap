//게임기능에 활용하는 전역변수 목록
let wordList = [];      //게임용 단어목록 저장을 위한 전역변수 배열
let answerList =[];     //게임용 각 질문당 정답여부를 기록하는 전역변수 배열
let printSet = 'title';   //게임용 질문의 형식을 지정하는 전역변수
let pronShow = false;    //게임용 발음부를 보일지 여부를 지정하는 전역변수
let wrongType = false;   //게임용 오답노트인지를 체크하는 전역변수

function confirmExit() {	
	const currentGame = getCurrentGame();
	
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
    $('#confirmModal').modal('hide');
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
		success:function(){
			console.log('입력성공');
		},
        error:function(e){
            console.log(e);
        }
	})
}