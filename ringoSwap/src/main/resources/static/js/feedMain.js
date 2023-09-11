let selectedImages = [];
let saved_feedNum = 0;  //History API를 사용한 피드 번호를 이용한 피드 출력기능에서 피드 번호 전달에 사용하는 전역변수

$(document).ready(function() {

	feedPrint();
    $(".feed-header").click(otherUserProfileButton);
    $(".profile-card").click(goToProfile);
	$(".feed-create-area").blur(collapseWrite);
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
    $(document).on('click', '.like-button', clickLikeFeed);
    $(document).on('click', '.replyLike', clickLikeReply);
	$(document).on('click', '.collapseFeed', function(event) {
	    if (!$(event.target).hasClass('bi')) {
	        feedDetail.call(this, event);
	    }
	});
	$(document).on('click', '#backToFeed', returnFeedMain);
	$(document).on('click', '.feed-delete-button', feedDelete);
	$(document).on('click', '.reply-delete-button', replyDelete);
	$(document).on('click', '.follower-btn', followerSearch);
	$(document).on('click', '.follow-btn', followeeSearch);
	
    //브라우저에서 뒤로가기 클릭시, History API이 적용된 feedDetail이 아닌 기존 페이지로 이동하는 이벤트
    window.addEventListener('popstate', function(event) {
        if (event.state) {
            // event.state를 기반으로 필요한 작업을 수행합니다.
            // 예: URL에 따른 페이지 콘텐츠를 로드하거나 특정 동작을 수행합니다.
            console.log(event.state);
            history.pushState({ feed_num : event.state.feed_num }, '', `?feed=${event.state.feed_num}`);
            location.reload();
        } else{
            // 페이지가 로드될 때 한 페이지 뒤로 가려면 이전 페이지로 이동해 호출합니다.
            history.replaceState(null, '', ``);
            location.reload();
        }
    });
    //History API를 사용한 새로고침시에도 피드가 유지되도록 하는 이벤트
    window.addEventListener('load', function () {
        const feedNum = getUrlParam('feed');
        if (feedNum) {
            // 파일 번호가 URL에 있을 경우 해당 텍스트 객체 열기
            saved_feedNum = feedNum;
            console.log('객체 열기');
            feedDetail();
        }
    });
});

//URL로부터 파일 번호를 얻어오는 함수
function getUrlParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

function feedPrint() {
	console.log("피드프린트")
	$.ajax({
		url: "feedPrintAll",
		type: "post",
		data: {
			feedArrayType: "default"
		},
		success: function(res) {
			
			let feeds = res.feedList;
			let likeCheck = res.likeCheckMap;
			
			$(".feed-display-area .col-12").empty();
			$("#feedDetail").hide();
			
			console.log(feeds);
			
			feeds.forEach(feed => {
				feedPhotoPrint(feed.feed_num);
				 // 좋아요 체크 값
			     let isLiked = likeCheck[feed.feed_num];
			     // 좋아요 버튼의 기본 클래스와 스타일을 설정
			     let likeButtonClass = "bi-heart";
			     // 좋아요 체크 값이 1이면, 버튼의 클래스와 스타일을 업데이트
			     if (isLiked === 1) {
			        likeButtonClass = "bi-heart-fill";
			     }
			     console.log(feed);
				$('.feed-display-area .col-12').append(`
                    <div class="card feed-card" data-feed-num="${feed.feed_num}">
                        <div class="card-header feed-header"> 
                            <span>${feed.user_id}</span>
                            <button type="button" class="btn btn-outline-danger btn-sm feed-delete-button position-absolute top-0 end-0 mt-1 me-2" data-feed-num="${feed.feed_num}">삭제</button>
                        </div>
                        <div class="card-body">
                            <p class="card-text collapseFeed" data-feed-num="${feed.feed_num}">${feed.contents}</p>
                            <div class="feed-image-list" data-feed-num="${feed.feed_num}"></div>
                            <div class="feed-button">
                                <span class="like-count" data-feed-num="${feed.feed_num}">${feed.like_count}</span> 
                                <i class="bi ${likeButtonClass} like-button" data-feed-num="${feed.feed_num}"></i> 
                                <i class="bi bi-chat reply"></i> 
                                <i class="bi bi-translate translate"></i>
                            </div>
                        </div>
                    </div>
                `);
            });
            $(".feed-display-area .col-12").show();
        },
        error: function(error) {
			console.log(error);
		}
	})
}

function feedDetail() {
	$('#feedDetail').empty();
	let feedNum = 0;
    let num = $(this).data('feed-num');
    if(num){   //this값이 있을 경우 
        feedNum = num;
    }else{
        feedNum = saved_feedNum;
    }
    
	$.ajax({
		url: "feedPrint",
		type: "post",
		data: {feed_num : feedNum},
		success: function(detail) {
			//기존 영역 숨기고 feedDetail 표시
			$(".feed-display-area .col-12").hide();
			$(".left-area, .middle-area").hide();
			//사진과 댓글 출력
			feedPhotoPrint(detail.feed.feed_num);
			replyPrint(detail.feed.feed_num);
			
		     let likeButtonClass = "bi-heart";
		     if (detail.likeCheck === 1) {
		        likeButtonClass = "bi-heart-fill";
		     }
			
			$('#feedDetail').append(`
                    <div class="card feed-card detail.feed" data-feed-num="${detail.feed.feed_num}">
                    <div class="card-header" style="width: 100%;">
                        <img src="../member/memberProfilePrint?user_id=${detail.feed.user_id}" alt="Poster Image" class="posterImage"> 
                        <span>${detail.feed.user_id}</span>
                        <button id="backToFeed" class="btn btn-link" class="btn btn-link position-absolute top-0 end-0 mt-3 me-8">
                            <i class="bi bi-arrow-return-left returnFeedMain"></i>
                        </button>
                        <button type="button" class="btn btn-outline-danger btn-sm feed-delete-button position-absolute top-0 end-0 mt-3 me-3">삭제</button>
                    </div>
                    <div class="card-body">
                        <p class="card-text">${detail.feed.contents}</p>
                        <div class="feed-image-list" data-feed-num="${detail.feed.feed_num}"></div>
                        <div class="feed-button">
                            <span class="like-count" data-feed-num="${detail.feed.feed_num}">${detail.feed.like_count}</span> 
                            <i class="bi ${likeButtonClass} like-button" data-feed-num="${detail.feed.feed_num}"></i> 
                            <i class="bi bi-translate translate"></i>
                        </div>
                        <div class="comment-input-section d-flex replyMargin" data-feed-num="${detail.feed.feed_num}">
                            <input type="text" class="form-control flex-grow-1 replyContent" placeholder="댓글을 입력하세요..." />
                            <button class="btn btn-primary ml-2 insertReply" style="min-width: 60px;">작성</button>
                        </div>
                        <div class="replyPrint"/>
                    </div>
                </div>
            `);
            
            history.pushState({ feed_num : detail.feed.feed_num }, '', `?feed=${detail.feed.feed_num}`);
			$("#feedDetail").show();
		}, 
		error: function(error) {
			console.error(error);
		}
	})
}

function feedPhotoPrint(feed_num) {
    let photoContainer = null;
    $.ajax({
        url: "feedPhotoPrint",
        type: "post",
        data: { feed_num: feed_num }, 
        success: function(photos) {
			photoContainer = document.querySelector(`.feed-image-list[data-feed-num="${feed_num}"]`);
            if (photos.length > 1 || photos !== null || photos !== undefined) {
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
	const replyContent = $(this).closest('.replyMargin').find(".replyContent").val();
    const feedNum = $(this).closest('.replyMargin').data('feed-num');
	console.log(replyContent, feedNum);
	
	$.ajax({
		url: "replyInsert",
		type: "post",
		data: { feed_num: feedNum, contents: replyContent },
        dataType:'json',
		success: function(feedNum) {
			replyPrint(feedNum);
		},
		error: function(error) {
			console.error(error);
		}
	})
}

function replyPrint(feedNum) {
	$.ajax({
		url: "replyPrint",
		type: "post",
		data: {feed_num: feedNum},
		success: function(res) {
			$(".replyPrint").empty();
			let replys = res.replyList;
			let likeCheck = res.likeCheckMap;
			replys.forEach(reply => {
                // 좋아요 체크 값
                let isLiked = likeCheck[reply.reply_num];
                // 좋아요 버튼의 기본 클래스와 스타일을 설정
                let likeButtonClass = "bi-heart";
                // 좋아요 체크 값이 1이면, 버튼의 클래스와 스타일을 업데이트
                if (isLiked === 1) {
                   likeButtonClass = "bi-heart-fill";
                }

				$(".replyPrint").append(`
                <div class="comment-list replyMargin">
                    <div class="comment-item d-flex">
                        <div class="comment-text flex-grow-1">${reply.contents}</div>
                        <span class="like-count" data-reply-num="${reply.reply_num}">${reply.like_count}</span> 
                        <i class="bi ${likeButtonClass} unLike replyLike" data-reply-num="${reply.reply_num}"></i>
                        <button class="btn btn-primary reply-btn ml-2">답글</button>
                        <button class="btn btn-primary reply-delete-button" data-reply-num="${reply.reply_num}">삭제</button>
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

//피드 작성칸 확대
function collapseWrite() {
	$(".emojionearea-editor").css("height", "100px");
	
	$(".feed-create-area .icons").hide();
	$(".feed-create-area .post").hide();

}

//피드 좋아요 기능
function clickLikeFeed() {
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
//댓글 좋아요 기능
function clickLikeReply() {
    const replyNum = $(this).data('reply-num');
    const buttonElement = $(this); // $(this) 참조를 저장

    $.ajax({
        url: "replyLikeClicker",
        type: "post",
        data: {reply_num: replyNum},
        success: function(like) {
            $('.like-count[data-reply-num="' + replyNum + '"]').text(like); // 좋아요 개수 업데이트

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

function returnFeedMain() {
    //pushState를 통해 브라우저에 feedMain초기화면 상태로 새 History 페이지를 추가.
    history.replaceState(null, '', '?feed=');

	$(".feed-display-area .col-12").show();
	$(".left-area, .middle-area").show();
	$("#feedDetail").hide();
}

function otherUserProfileButton(event) {
	event.preventDefault();
}

function feedDelete(){
    let feed_num = $(this).data('feed-num');
    console.log(feed_num);
    $.ajax({
        url: "feedDeleteOne",
        type: "post",
        data: {feed_num : feed_num},
        dataType:'json',
        success:function(res){
			if(res === "0") {
				alert("본인이 작성한 글만 삭제할 수 있습니다.")
			} else { 
				confirm("피드를 삭제하시겠습니까?");	
			}
			feedPrint();
        },
        error: function(error) {
            console.log(error);
        }
    })
}

function replyDelete(){
    let reply_num = $(this).data('reply-num');
    let feed_num = $('.replyMargin').data('feed-num');
    console.log(reply_num);
	
    $.ajax({
        url: "replyDeleteOne",
        type: "post",
        data: {reply_num : reply_num},
        dataType:'json',
        success:function(res){
			if(res === "0") {
				alert("본인이 작성한 글만 삭제할 수 있습니다.")
			} 
			replyPrint(feed_num);
        },
        error: function(error) {
            console.log(error);
        }
    })
}

/* 팔로워, 팔로우 검색 및 출력기능 */
function followerSearch(){
    let username = $('.justify-content-center .searchFollower').val();
    $.ajax({
        url: "followerSearch",
        type: "post",
        data: {username : username},
        dataType:'json',
        success:function(followerList){
            if(followerList){
                console.log(followerList);
            }else{
                console.log('팔로워 검색');
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
}
function followeeSearch(){
    let username = $('.justify-content-center .searchFollow').val();
    $.ajax({
        url: "followeeSearch",
        type: "post",
        data: {username : username},
        dataType:'json',
        success:function(followeeList){
            if(followeeList){
                console.log(followeeList);
            }else{
                console.log('팔로우 검색');
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
}