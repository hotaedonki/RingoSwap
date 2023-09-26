const cards = [...$(".flashCards-question-box")]; // 모든 카드들
let currentCardIndex = 0;          //게임용 단어목록 순서를 저장하는 전역변수

function playFlashCards() {
    //history API기능을 위한 url변수
    const currentUrl = window.location.href;
    const newUrl = 'http://localhost:8888/ringo/game/gameMain?category=flashcard';

    flashCardsHTML = `
	    <section class="home container flashCards-container">
			<div class="row flashCards-option-line">
				<div class="col-3 d-flex flex-row">
					<i class="bi bi-arrow-return-left" data-bs-toggle="modal" data-bs-target="#confirmModal"></i>
				</div>
				<div class="col-6 currentProblemNumber d-flex justify-content-center">
					<span id="currentNum">1</span>/<span id="totalNum">50</span> 
			    </div>
				<div class="col-3 update-cards d-flex flex-row-reverse">
					<button type="button" class="btn btn-primary btn-sm">Update</button>
				</div>
			</div>
			<div class="row d-flex justify-content-center">
				<div class="card border-dark flashCards-question-box mb-3 mt-5">
					<div class="flashCards-inner d-flex justify-content-center align-items-center">
						<div class="flashCards-front">
							<!-- 문제 내용 -->
							<h1 id="front-print">Front</h1>
							<h1 class="pron-print">[발음]</h1>
						</div>
						<div class="flashCards-back">
							<!-- 답안 내용 -->
							<h1 id="back-print">Back</h1>
							<h1 class="pron-print">[발음]</h1>
						</div>
					</div>
				</div>
			</div>
		</section>`;
		$('.main-container').hide();
		$("body").append(flashCardsHTML);
        if(currentUrl !== newUrl){
            history.pushState({ category:'flashcard', url: newUrl }, '', `?category=flashcard`);
        }
        flashCardsQuestionSave();
}

//단어목록을 전역변수에 저장
function flashCardsQuestionSave(){
    $.ajax({
        url: 'gameNotePrint',
        type:'post',
        data:{category : 'flashCards'},
        dataType:'json',
        success:function(res){
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

            wordList = res.wordList;
            console.log(wordList);
            currentCardIndex = 0;      //count변수 초기화
            $('#totalNum').text(res.wordList.length);      //문제 갯수 총합 출력
        	flashCardsQuestionPrint();
        },
        error:function(e){
            console.log(e);
        }
    })
}
//저장한 전역변수를 순서대로 출력
function flashCardsQuestionPrint(){
    let word = wordList[currentCardIndex];
    $('#currentNum').text(currentCardIndex + 1);		//현재 문제순번 출력

    if(printSet ==='title'){
        $('#front-print').text(word.word);
        $('#back-print').text(word.mean);
    }else{
        $('#front-print').text(word.mean);
        $('#back-print').text(word.word);
    }
    if(pronShow){
        $('.pron-print').text(word.pron);
    }else{
        $('.pron-print').hide();
    }
    
    console.log('출력끝');
}

function clickCard() {
    const innerCard = $(this).find('.flashCards-inner');
    console.log(innerCard.hasClass('flipped'));
    
    if (innerCard.hasClass('flipped')) {
        nextCard();
        return;
    } else {
        innerCard.css('transform', 'rotateY(180deg)').addClass('flipped');
    }
}

function nextCard() {
    //카운트값을 +1하고, 해당 값이 단어갯수를 넘는지 확인
    currentCardIndex++;
    if(currentCardIndex >= wordList.length){
        //마지막 순번에서 이 함수 호출시, 알림을 출력하고 gameMain페이지로 돌아간다.
	    alert("끝!");
        returnToGameMain();
    }
    // 카드를 다시 앞면으로 변경
    $('.flashCards-inner').removeClass('flipped');
    $('.flashCards-inner').css('transform', 'rotateY(0deg)');

    //다음 word정보를 카드에 출력
    flashCardsQuestionPrint();
}
