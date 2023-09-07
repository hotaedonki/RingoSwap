$(document).ready(function() {
	feedPrint();
    $(".bi-heart, .bi-heart-fill").click(clickLike);
    $(".feed-header").click(otherUserProfileButton);
    $(".profile-card").click(goToProfile);
    $(".collapseFeed").click(feedDetail);
    $("#backToFeed").click(returnFeed);
	$(".feed-create-area").blur(collapseFeed);
	
    let eA = $("#chatInput").emojioneArea({
        filtersPosition: "bottom",
        pickerPosition: "bottom",
    });

    eA[0].emojioneArea.on("focus", function() {
        // 텍스트 영역의 높이를 150px로 변경
        $(".emojionearea-editor").css("height", "150px");

        // 아이콘 및 포스트 버튼 표시
        $(".feed-create-area .icons").show();
        $(".feed-create-area .post").show();
    });
});

function feedPrint() {
	
}

function collapseFeed() {
	$(".emojionearea-editor").css("height", "100px");
	
	$(".feed-create-area .icons").hide();
	$(".feed-create-area .post").hide();
}

function openChatBox() {
	let targetChatBox = $(this).data("chat-target");
	
	$(".chat-box").hide();
	$(targetChatBox).show();
};

function clickLike() {
	$.post("feedLikeClicker" {
		feed_num: feed_num
	}).done(function() {
		$(this).toggleClass('bi-heart bi-heart-fill');
		if ($(this).hasClass('bi-heart-fill')) {
	        $(this).css('color', 'red');
	    } else {
	        $(this).css('color', 'black');
	    }
	}).fail(function() {
		console.log("좋아요 실패");
	})
}

function goToProfile() {
	window.location.href = '../member/myPage';
}

function feedDetail() {
    $(".left-area, .middle-area").hide();
    
    // feed-detail 표시
    $("#feedDetail").show();
}

function returnFeed() {
    // feed-detail 숨기기
    $("#feedDetail").hide();
    
	// 원래의 영역을 다시 표시
    $(".left-area, .middle-area").show();
}

function otherUserProfileButton(event) {
	event.preventDefault();
}


