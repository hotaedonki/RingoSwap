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
    if ($('.flashCards-result-container').length > 0) return 'flashCardsResult';
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
	const resultGame = getCurrentGame();
	console.log(resultGame + "-container");
	$(`.${resultGame}-container`).remove();
	
	$('#returnToMainModal').modal('hide');
	$('.main-container').show();
}

function questionNumPrint(){
    $.ajax({
        url:'questNumPrint',
        type:'post',
        dataType:'json',
        success:function(number){
            console.log('문제갯수 : '+number);
            if(number === 0){
                number = 'total';
            }
            $('.btn[data-bs-target="#countOfItemsModal"]').text(number);
        },
        error:function(e){
            console.error(e);
        }
    })
}