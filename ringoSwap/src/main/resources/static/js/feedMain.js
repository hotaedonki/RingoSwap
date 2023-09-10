let selectedImages = [];

$(document).ready(function() {
	feedPrint();
    $(".feed-header").click(otherUserProfileButton);
    $(".profile-card").click(goToProfile);
    $("#backToFeed").click(returnFeed);
	$(".feed-create-area").blur(collapseFeed);
	$(".post").on('click', createPost);
	$("#imageIcon").on('click', function() {
		$("#imageInput").click();
	});
	$("#imageInput").on('change', function(event) {
		const files = event.target.files;

		//선택된 이미지 파일을 배열에 저장 후 미리보기 생성.
		for (let i = 0; i < files.length; i++) {
			selectedImages.push(files[i]);
			
			const imgElement = document.createElement('img');
			imgElement.src = URL.createObjectURL(files[i]);
			imgElement.classList.add('image-preview');
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
    $(document).on('click', '.insertReply', replyInsert);
    $(document).on('click', '.unLike, .like', clickLike)
	$(document).on('click', '.collapseFeed', function(event) {
	    if (!$(event.target).hasClass('bi')) {
	        feedDetail.call(this, event);
	    }
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
			feeds.forEach(feed => {
				feedPhotoPrint(feed.feed_num);
				$('.feed-display-area .col-12').append(`
                    <div class="card feed-card collapseFeed" data-feed-num="${feed.feed_num}">
                        <div class="card-header feed-header" onclick="event.stopPropagation();"> 
                            <span>${feed.user_id}</span>
                        </div>
                        <div class="card-body">
                            <p class="card-text">${feed.contents}</p>
                            <div class="feed-image-list" data-feed-num="${feed.feed_num}"></div>
                            <div class="feed-button">
                                <span class="like-count" data-feed-num="${feed.feed_num}">${feed.like_num}</span> 
                                <i class="bi bi-heart unLike" data-feed-num="${feed.feed_num}"></i> 
                                <i class="bi bi-chat reply"></i> 
                                <i class="bi bi-translate translate"></i>
                            </div>
                        </div>
                    </div>
                `);
            });
        },
        error: function(error) {
			console.log(error);
		}
	})
}

function feedDetail() {
	const feedNum = $(this).data('feed-num');
	$.ajax({
		url: "feedPrint",
		type: "post",
		data: {feed_num : feedNum},
		success: function(clickedFeed) {
			$(".feed-display-area .col-12").empty();
			$(".left-area, .middle-area").hide();
			feedPhotoPrint(clickedFeed.feed_num);
			replyPrint(clickedFeed.feed_num);
			$('#feedDetail').append(`
                    <div class="card feed-card clickedFeed" data-feed-num="${clickedFeed.feed_num}">
                    <div class="card-header" style="width: 100%;">
                        <img src="../img/고양.jpg" alt="Poster Image" class="posterImage"> 
                        <span>${clickedFeed.user_id}</span>
                        <button id="backToFeed" class="btn btn-link">
                            <i class="bi bi-arrow-return-left"></i>
                        </button>
                    </div>
                    <div class="card-body">
                        <p class="card-text">${clickedFeed.contents}</p>
                        <div class="feed-image-list" data-feed-num="${clickedFeed.feed_num}"></div>
                        <div class="feed-button">
                            <span class="like-count" data-feed-num="${clickedFeed.feed_num}">${clickedFeed.like_num}</span> 
                            <i class="bi bi-heart unLike" data-feed-num="${clickedFeed.feed_num}"></i> 
                            <i class="bi bi-translate translate"></i>
                        </div>
                        <div class="comment-input-section d-flex replyMargin">
                            <input type="text" class="form-control flex-grow-1 replyContent" placeholder="댓글을 입력하세요..." />
                            <button class="btn btn-primary ml-2 insertReply" style="min-width: 60px;">작성</button>
                        </div>
                        <div class="replyPrint"/>
                    </div>
                </div>
            `);
			$("#feedDetail").show();
		}, 
		error: function(error) {
			console.error(error);
		}
	})
}

function feedPhotoPrint(feed_num) {
    $.ajax({
        url: "feedPhotoPrint",
        type: "post",
        data: { feed_num: feed_num }, 
        success: function(photos) {
			let photoContainer = document.querySelector(`.feed-image-list[data-feed-num="${feed_num}"]`);
            if (photos.length > 1) {
                photoContainer.classList.add('multi-image'); // 여러 이미지가 있는 경우에 대한 CSS 클래스 추가
            }
            photos.forEach(photo => {
                const photoElem = document.createElement('img');
                photoElem.src = 'data:image/jpeg;base64,' + photo.fileData; 
                photoElem.alt = photo.fileName;  
                photoElem.classList.add('feed-photo');
                if (photos.length > 1) {
                    photoElem.classList.add('multi'); // 여러 이미지가 있는 경우에 대한 CSS 클래스 추가
                } 
                photoElem.addEventListener('click', () => {
					$('#imageModal .modal-body').html(''); // 모달 body를 비웁니다

				    const originalImage = document.createElement('img'); // 새 이미지 객체 생성
				    originalImage.src = 'data:image/jpeg;base64,' + photo.fileData; // 원본 이미지 URL 설정
				
				    originalImage.onload = () => { // 이미지 로딩이 완료되면 실행
				        if (originalImage.naturalWidth > 800 || originalImage.naturalHeight > 600) {
				            originalImage.style.width = '800px';  // 이미지 너비 설정
				            originalImage.style.height = 'auto';  // 이미지 높이를 auto로 설정하여 비율 유지
				        }
				        $('.modal-content').css({
					        width: originalImage.width + 'px',
					        height: originalImage.height + 'px'
					    });
				    };

				    $('#imageModal .modal-body').append(originalImage); // 원본 이미지를 모달 body에 추가
				    $('#imageModal').modal('show'); // 모달을 표시합니다
				    });
                photoContainer.appendChild(photoElem);
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
	
    for (let i = 0; i < selectedImages.length; i++) {
        feedData.append('photos', selectedImages[i]);
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
            $('#imagePreviewContainer').html(''); 
            selectedImages = [];
            feedPrint();             // 피드를 다시 로드합니다
        },
        error: function(error) {
            console.error(error);
        }
    });
}

function replyInsert() {
	const replyContent = $(".replyContent").val();
	const feedNum = $(".clickedFeed").data('feed-num');
	console.log(replyContent, feedNum);
	
	$.ajax({
		url: "replyInsert",
		type: "post",
		data: { feed_num: feedNum, contents: replyContent },
		success: function(feedNum) {
			replyPrint(feedNum);
		},
		error: function(error) {
			console.error(error)
		}
	})
}

function replyPrint(feedNum) {
	$.ajax({
		url: "replyPrint",
		type: "post",
		data: {feed_num: feedNum},
		success: function(replys) {
			$(".replyPrint").empty();
			replys.forEach(reply => {
				$(".replyPrint").append(`
                <div class="comment-list replyMargin">
                    <div class="comment-item d-flex">
                        <div class="comment-text flex-grow-1">${reply.contents}</div>
                        <i class="bi bi-heart unLike replyLike"></i>
                        <button class="btn btn-primary reply-btn ml-2">답글</button>
                        <button class="btn btn-primary reply-delete-btn">삭제</button>
                        <div class="reply-input-section" style="display: none;">
                            <input type="text" class="form-control" placeholder="답글을 입력하세요..." />
                            <button class="btn btn-primary">작성</button>
                            
                        </div>
                        <div class="reply-list replyMargin" style="display: none;">
                            <div class="reply-item">
                                <div class="reply-text">임시 답글 내용</div>
                            </div>
                        </div>
                    </div>
                </div>`
                )
			})
		},
		error : function(error) {
			console.error(error);
		}
	})
}

//피드 확대 코드
function collapseFeed() {
	$(".emojionearea-editor").css("height", "100px");
	
	$(".feed-create-area .icons").hide();
	$(".feed-create-area .post").hide();
}

function clickLike() {
    const feedNum = $(this).data('feed-num');
    const buttonElement = $(this); // $(this) 참조를 저장

    $.ajax({
        url: "feedLikeClicker",
        type: "post",
        data: {feed_num: feedNum},
        success: function(like) {
            $('.like-count[data-feed-num="' + feedNum + '"]').text(like); // 좋아요 개수 업데이트

            buttonElement.toggleClass('bi-heart bi-heart-fill');
            if (buttonElement.hasClass('bi-heart-fill')) {
                buttonElement.css('color', 'red');
            } else {
                buttonElement.css('color', ''); // 기본 색상으로 설정
            }
        },
        error: function(error) {
            console.error(error);
        }
    })
}



function goToProfile() {
	window.location.href = '../member/myPage';
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
