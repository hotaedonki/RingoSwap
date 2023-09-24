function confirmExit() {	
	const currentGame = getCurrentGame();
	
	console.log(currentGame);
		if(currentGame === 'MCQ' || currentGame === 'dictation') {
			$(`.${currentGame}-container`).remove();
			window[`${currentGame}ResultScreen`]();
		} else if (currentGame === 'flashCards') {
			$('.flashCards-container').remove();
			$('.main-container').show();
		}
    $('#confirmModal').modal('hide');
}

function getCurrentGame() {
    if ($('.MCQ-container').length > 0) return 'MCQ';
    if ($('.dictation-container').length > 0) return 'dictation';
    if ($('.flashCards-container').length > 0) return 'flashCards';
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
