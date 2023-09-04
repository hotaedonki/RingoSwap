$(document).ready(function() {
	$(".open-chatbox").click(openChatBox);
	$("#send-message").click(sendMessage);
	$(".bi-heart, .bi-heart-fill").click(clickLike);
	$(".goProfile").click(goProfile);
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