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

function feedSearch(){
    //id값이 searchInput인 검색창에서 커서가 벗어났을때 실행되는 피드목록 검색 함수
    feedPrint();
}

function returnFeedMain() {
	//history를 쓰면 사진값을 못받아옴
    window.location.href= '../feed/feedMain';
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

$(document).ready(function() {
    const offcanvasElement = document.getElementById('offcanvasWithBothOptions');
    
    offcanvasElement.addEventListener('show.bs.offcanvas', function() {
        // Offcanvas가 보여질 때, 팔로우와 팔로워 모달의 z-index를 1035로 설정
        $('#followerModal, #followModal').css('z-index', '1035');
        $('.modal-backdrop').css('z-index', '1040');
    });

    offcanvasElement.addEventListener('hidden.bs.offcanvas', function() {
        // Offcanvas가 숨겨질 때, 팔로우와 팔로워 모달의 z-index를 1055로 설정
        $('#followerModal, #followModal').css('z-index', '1055');
    });
});
