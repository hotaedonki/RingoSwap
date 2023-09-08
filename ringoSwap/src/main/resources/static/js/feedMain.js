let selectedImages = [];

$(document).ready(function() {
	feedPrint();
	feedPhotoPrint();
    $(".bi-heart, .bi-heart-fill").click(clickLike);
    $(".feed-header").click(otherUserProfileButton);
    $(".profile-card").click(goToProfile);
    $(".collapseFeed").click(feedDetail);
    $("#backToFeed").click(returnFeed);
	$(".feed-create-area").blur(collapseFeed);
	$(".post").on('click', createPost);
	$("#imageIcon").on('click', function() {
		$("#imageInput").click();
	});
	$("#imageInput").on('change', function(event) {
		const files = event.target.files;
		// 미리보기 컨테이너 비우기
		$("#imagePreviewContainer").empty();
		
		//선택된 이미지 파일을 배열에 저장 후 미리보기 생성.
		selectedImages = [];
		for (let i = 0; i < files.length; i++) {
			selectedImages.push(files[i]);
			
			const imgElement = document.createElement('img');
			imgElement.src = URL.createObjectURL(files[i]);
			imgElement.height = 100;
			$("#imagePreviewContainer").append(imgElement);
		}
	});
	
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
	$.ajax({
		url: "feedPrintAll",
		type: "post",
		data: {
			feedArrayType: "default"
		},
		success: function(feeds) {
			$(".feed-display-area .col-12").empty();
			feeds.forEach((feed, index) => {
				$('.feed-display-area .col-12').append(`
                    <div class="card feed-card collapseFeed">
                        <div class="card-header feed-header" onclick="event.stopPropagation();"> 
                            <span>${feed.user_id}</span>
                        </div>
                        <div class="card-body">
                            <p class="card-text">${feed.contents}</p>
                            <div class="image-list" id="image-list-${index}"></div>
                            <div class="feed-button" onclick="event.stopPropagation();">
                                <span>${feed.likes}</span> 
                                <i class="bi bi-heart unLike"></i> 
                                <i class="bi bi-chat reply"></i> 
                                <i class="bi bi-translate translate"></i>
                            </div>
                        </div>
                    </div>
                `);
                feedPhotoPrint(feed.feed_num, index);
            });
        },
        error: function(error) {
			console.log(error);
		}
	})
}

function feedPhotoPrint(feed_num, index) {
    $.ajax({
        url: "feedPhotoPrintAll",
        type: "post",
        data: { feed_num: feed_num },
        success: function(feedPhotos) {
            feedPhotos.forEach(feedPhoto => {
                $(`#image-list-${index}`).append(`
                    <img src="${feedPhoto.feedImage}" alt="Feed Image" class="feed-image">
                `);
            });
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function createPost() {
	let feedData = new FormData();
	feedData.append('content', $('#chatInput').val());
	
	let files = $('#imageInput')[0].files;
    for (let i = 0; i < files.length; i++) {
        feedData.append('photos', files[i]);
    }
    
    for (let pair of feedData.entries()) {
    console.log(pair[0]+ ', ' + pair[1]); 
	}

	$.ajax({
        url: "feedWrite",
        type: "post",
        processData: false, // 필수: jQuery가 데이터를 처리하지 않도록 설정
        contentType: false, // 필수: Content-Type 헤더를 설정하지 않도록 설정
        data: feedData,     // FormData 객체를 전송합니다
        success: function(response) {
			console.log("성공");
            let content = $(".emojionearea-editor").html();
		    $(".emojionearea-editor").html('');
            $('#imageInput').val(''); // 파일 입력 필드를 비웁니다
            feedPrint();             // 피드를 다시 로드합니다
        },
        error: function(error) {
            console.error(error);
        }
    });
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
	$.post("feedLikeClicker", {
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

function feedDelete(){
    let num = $(this).data('feed-num');
    console.log(num);
    $.ajax({
        url: "feedDeleteOne",
        type: "POST",
        data: {feed_num : num},
        dataType: 'json',
        success:function(str){
            console.log(str);
            feedPrint();
        },
        error: function(error) {
            // 에러 발생 시 처리
            console.log('에러');
            console.log(error);
        }
    })
}
