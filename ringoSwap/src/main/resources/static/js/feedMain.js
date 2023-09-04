$(document).ready(function() {
	$(".open-chatbox").click(openChatBox);
	$("#send-message").click(sendMessage);
	$(".bi-heart, .bi-heart-fill").click(clickLike);
	$(".profile-card").click(goToProfile);
	$(".feed-card").click(feedDetail)
});

function openChatBox() {
	let targetChatBox = $(this).data("chat-target");
	
	$(".chat-box").hide();
	$(targetChatBox).show();
};

/*function sendMessage() {
	let message = $("#chatInput").val();
	
	if(message) {
		$.post("sendMessage" {
			message : message
		})
		.done(function() {
			console.log("send")
			chatPrint();
		})
		.fail(function() {
			console.log("fail")
		});
	});
}*/

function clickLike() {
	$(this).toggleClass('bi-heart bi-heart-fill');
		if ($(this).hasClass('bi-heart-fill')) {
	        $(this).css('color', 'red');
	    } else {
	        $(this).css('color', 'black');
	    }
}

function goToProfile() {
	window.location.href = '../member/myPage';
}

function feedDetail() {
	$(".feed-card").on("click", function() {
        // 원래의 영역 숨기기
        $(".profile-area, .notification-area, .feed-area").hide();
        
        // feed-detail 표시
        $("#feedDetail").show();
    });

    // feed-detail의 "뒤로 가기" 버튼 클릭 이벤트
    $("#backToFeed").on("click", function() {
        // 원래의 영역을 다시 표시
        $(".profile-area, .notification-area, .feed-area").show();
        
        // feed-detail 숨기기
        $("#feedDetail").hide();
    });
}
