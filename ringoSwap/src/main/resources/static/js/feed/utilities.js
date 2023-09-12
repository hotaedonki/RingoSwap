function getUrlParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

function collapseWrite() {
	$(".emojionearea-editor").css("height", "100px");
	$(".feed-create-area .icons").hide();
	$(".feed-create-area .post").hide();
}

function goToProfile() {
	window.location.href = '../member/myPage';
}

function otherUserProfileButton(event) {
	event.preventDefault();
}

function returnFeedMain() {
    //pushState를 통해 브라우저에 feedMain초기화면 상태로 새 History 페이지를 추가.
    history.pushState(null, '', '?feed=');

	$(".feed-display-area .col-12").show();
	$(".left-area, .middle-area").show();
	$("#feedDetail").hide();
}

function timeDifference(current, previous) {
    const msPerMinute = 60 * 1000;
    const msPerHour = msPerMinute * 60;
    const msPerDay = msPerHour * 24;
    const msPerMonth = msPerDay * 30;
    const msPerYear = msPerDay * 365;
    const elapsed = current - previous;
    
	if (elapsed < msPerHour) {
        return Math.floor(elapsed/msPerMinute) + ' 분 전';   
    }
    else if (elapsed < msPerDay) {
        return Math.floor(elapsed/msPerHour ) + ' 시간 전';   
    }
    else if (elapsed < msPerMonth) {
        return Math.floor(elapsed/msPerDay) + ' 일 전';   
    }
    else if (elapsed < msPerYear) {
        return Math.floor(elapsed/msPerMonth) + ' 달 전';   
    }
    else {
        return Math.floor(elapsed/msPerYear) + ' 년 전';   
    }
}
