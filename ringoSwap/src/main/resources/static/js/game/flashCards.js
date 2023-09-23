let currentCardIndex = 0; // 현재 카드 인덱스
const cards = [...$(".flashCards-question-box")]; // 모든 카드들

function playFlashCards() {
    window.location.href = '../game/flashCards';
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

$(document).ready(function() {
	$('.flashCards-question-box').on('click', clickCard);
});
