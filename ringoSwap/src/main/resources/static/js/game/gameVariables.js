let previousUrl = null;  //handlePopState에서 사용할 뒤로가기/앞으로가기 판별용 전역변수. 현재 주소를 담는다.

$(document).ready(function () {
	//받아쓰기가 화면에 출력 중일 때 발음 맞히기 라디오버튼 출력 
	$('#carouselExampleCaptions').on('slide.bs.carousel', chooseDictation);
	
	//아래 3개는 각 기능에 대한 모달을 띄우는 기능
	$('.btn[data-bs-target="#wordlistModal"]').on('click', showWordListModal);
	$('.btn[data-bs-target="#formModal"]').on('click', showFormModal);
	$('.btn[data-bs-target="#countOfItemsModal"]').on('click', showCountOfItemsModal);
	
	//게임 클릭 시 해당 게임 화면 출력
	$('.MCQ').on('click', playMCQ);
	$('.flash-cards').on('click', playFlashCards);
	$('.dictation').on('click', playDictation);
	
	//플래시카드에서 정답 출력
	$('.flashCards-question-box').on('click', clickCard);
	
	//게임을 중단하고 결과보기
	$('.confirm-exit').on('click', confirmExit);
	
	//전체선택
	$(document).on('click', '.select-all', selectAll);
	//메인화면으로 돌아가기
	$(document).on('click', '.return-to-game-main', returnToGameMain);
	//윈도우이벤트핸들러(대부분 history api 기능)
	initializeWindowEventHandlers();

	//게임세팅정보 출력 기능
	questionNumPrint();
});


function initializeWindowEventHandlers() {
    window.addEventListener('popstate', popStateWindow);
    window.addEventListener('load', loadWindow);
}

//브라우저에서 뒤로가기 클릭시, History API이 적용된 feedDetail이 아닌 기존 페이지로 이동하는 이벤트
function popStateWindow(event) {
    let newUrl = window.location.href;
	console.log(event);
	console.log(event.category);
    if(previousUrl === newUrl){
        return; //현재 url과 이전 url이 동일할경우, 아무 작업도 수행하지 않도록 리턴
    }
    if (event.state && event.state.category) {
        // 이전 URL이 존재하고 현재 URL과 다른 경우, 앞으로 가기 작업을 실행
        console.log('앞으로 가기');
        loadWindow();
    } else {
        // 이전 URL이 없으면서 현재 URL과 다른 경우, 뒤로가기 작업
        console.log('뒤로 가기');
        returnToGameMain();
    }    
    previousUrl = newUrl; // 현재 URL을 이전 URL로 저장합니다.
}

//History API를 사용한 새로고침시에도 피드가 유지되도록 하는 이벤트
function loadWindow() {
	console.log("히스토리api")
    const category = getUrlParam('category');
    if (category === "flashcard") {
      // category가 flashcard일 경우 플래시카드 게임 실행
        console.log('flashcard 열기');
        playFlashCards();
        
    }else if(category === "dictation"){
        console.log('dictation 열기');
		playDictation();
	}else if(category === "MCQ"){
        console.log('MCQ 열기');
		playMCQ();
	}else{
		console.log('메인화면인가?');
		if(!category){
			returnToGameMain();
		}
	}
}

function getUrlParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}