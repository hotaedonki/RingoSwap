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
});
