let currentCardIndex = 0; // 현재 카드 인덱스
const cards = [...$(".flashCards-question-box")]; // 모든 카드들

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
					1/50</div>
				<div class="col-3 update-cards d-flex flex-row-reverse">
					<button type="button" class="btn btn-primary btn-sm">Update</button>
				</div>
			</div>
			<div class="row d-flex justify-content-center">
				<div class="card border-dark flashCards-question-box mb-3 mt-5">
					<div class="flashCards-inner d-flex justify-content-center align-items-center">
						<div class="flashCards-front">
							<!-- 문제 내용 -->
							<h1>Front</h1>
							<h1>[발음]</h1>
						</div>
						<div class="flashCards-back">
							<!-- 답안 내용 -->
							<h1>Back</h1>
							<h1>[발음]</h1>
						</div>
					</div>
				</div>
			</div>
		</section>`;
		$('.main-container').hide();
		$("body").append(flashCardsHTML);
        if(currentUrl !== newUrl){
            history.pushState({ url: newUrl }, '', `?category=flashcard`);
        }
}

function clickCard() {
    const innerCard = $(this).find('.flashCards-inner');

    if (innerCard.hasClass('flipped')) {
        nextCard();
        return;
    } else {
        innerCard.css('transform', 'rotateY(180deg)').addClass('flipped');
    }
}

function nextCard() {
	alert("끝!");
}
