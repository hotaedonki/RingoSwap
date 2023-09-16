function feedPrint(optionalRes) {
    let text = $('#searchInput').val();
    console.log(text);
    
    if (optionalRes) {
		console.log("해시태그 클릭 후 feedPrint 반환 값 ", optionalRes)
        renderFeeds(optionalRes);
        return;
    }

    $.ajax({
        url: "../feed/feedPrintAll",
        type: "post",
        data: {
            feedArrayType: "default",
            text : text
        },
        success: function(res) {	
            renderFeeds(res);
        },
        error: function(error) {
            console.log(error);
        }
    })
}


function renderFeeds(res) {
	console.log("renderFeeds의 res값: ", res)
	let feeds = res.feedList;
	let likeCheck = res.likeCheckMap;
	
	$(".feed-display-area .col-12").empty();
    $(".left-area, .middle-area").show();
	$("#feedDetail").hide();
	
	console.log(feeds);
	
	feeds.forEach(feed => {
		feedPhotoPrint(feed.feed_num);
	     let likeButtonClass = likeCheck[feed.feed_num] === 1 ? "bi-heart-fill" : "bi-heart";
		 //해시태그에 css적용
		 let styledContent = hashtagHighlightAndClick(feed.contents);
	     console.log(feed);
		$('.feed-display-area .col-12').append(`
            <div class="card feed-card" data-feed-num="${feed.feed_num}">
                <div class="card-header feed-header goToOtherProfile" data-user-name="${feed.username}"> 
                	<img src="../member/memberProfilePrint?user_id=${feed.user_id}" alt="Poster Image" class="posterImage"> 
                    <span class="feedUser" data-username="${feed.username}">${feed.username}</span>
                    <button type="button" class="btn btn-outline-danger btn-sm feed-delete-button position-absolute top-0 end-0 mt-1 me-2" data-feed-num="${feed.feed_num}">삭제</button>
                </div>
                <div class="card-body">
                    <p class="card-text collapseFeed" data-feed-num="${feed.feed_num}">${styledContent}</p>
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
    console.log('완성');
}

function feedDetail() {
	$('#feedDetail').empty();
    let num = $(this).data('feed-num');
    if(num){ 
        feedNum = num;
    } else {
		feedNum = saved_feedNum;
	}
    /*
    history api를 사용한 히스토리 스택 기능에 feedDetail로 호출된 URL이 중복해서 입력되지 않도록 하기위한 변수 2개
    */
    const currentUrl = window.location.href;
    const newUrl = 'http://localhost:8888/ringo/feed/feedMain?feed='+feedNum;

	$.ajax({
		url: "feedPrint",
		type: "post",
		data: {feed_num : feedNum},
		success: function(detail) {
			console.log(detail.feed);
			$(".feed-display-area .col-12").hide();
			$(".left-area, .middle-area").hide();
			//사진과 댓글 출력
			feedPhotoPrint(detail.feed.feed_num);
			replyPrint(detail.feed.feed_num);
			
		    let likeButtonClass = detail.likeCheck === 1 ? "bi-heart-fill" : "bi-heart";
		     
			let styledContent = hashtagHighlightAndClick(detail.feed.contents);
             
            $('#feedDetail').append(`
                    <div class="card feed-card" data-feed-num="${detail.feed.feed_num}">
                    <div class="card-header goToOtherProfile" style="width: 100%;" data-user-id="${detail.feed.user_id}">
                        <img src="../member/memberProfilePrint?user_id=${detail.feed.user_id}" alt="Poster Image" class="posterImage"> 
                        <span>${detail.feed.user_id}</span>
                        <button id="backToFeed" class="btn btn-link" class="btn btn-link position-absolute top-0 end-0 mt-3 me-8">
                            <i class="bi bi-arrow-return-left returnFeedMain"></i>
                        </button>
                        <button type="button" class="btn btn-outline-danger btn-sm feed-delete-button position-absolute top-0 end-0 mt-3 me-3" data-feed-num="${detail.feed.feed_num}">삭제</button>
                    </div>
                    <div class="card-body">
                        <p class="card-text">${styledContent}</p>
                        <div class="feed-image-list" data-feed-num="${detail.feed.feed_num}"></div>
                        <div class="feed-button">
                            <span class="like-count" data-feed-num="${detail.feed.feed_num}">${detail.feed.like_count}</span> 
                            <i class="bi ${likeButtonClass} like-button" data-feed-num="${detail.feed.feed_num}"></i> 
                            <i class="bi bi-translate translate"></i>
                        </div>
                        <div class="comment-input-section d-flex replyMargin">
                            <input type="text" class="form-control flex-grow-1 replyContent follow-search-input" placeholder="댓글을 입력하세요..." data-feed-num="${detail.feed.feed_num}" />
                            <button class="btn btn-primary ml-2 insertReply" style="min-width: 60px;" data-feed-num="${detail.feed.feed_num}">작성</button>
                        </div>
                        <div class="replyPrint"></div>
                    </div>
                </div>
            `);
            console.log("히스토리 : ", history);
			$("#feedDetail").show();
			if(currentUrl !== newUrl){
                history.pushState({ feed_num : detail.feed.feed_num, url: newUrl }, '', `?feed=${detail.feed.feed_num}`);
            }
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
	const content = $('#chatInput').val();
	feedData.append('content', content);
	
	const hashtags = content.match(/#[^\s#]+/g) || [];
	feedData.append('hashtagsJson', JSON.stringify(hashtags));
	
	console.log("피드 데이터와 해시태그 값 : ", $('#chatInput').val(), hashtags);
	
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
                followerList.forEach(follower => {
                    $('.followerBox').append(`
                    <div><img src="" alt="Poster Image" class="posterImage feedUser" data-username="${follower.follower_name}"> 
                        <img src="../member/memberProfilePrint?user_id=${follower.follower_num}" alt="Profile Picture" />
                        <span >${follower.follower_name}</span>
                        <img src=".../img/영어.jpg" alt="Native Language" />
                        <img src=".../img/일본어.jpg" alt="Learning Language" />
                        <button type="button" class="btn btn-primary">팔로우</button>
                    </div>
                    `);
                })
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
                followeeList.forEach(followee => {
                    $('.followBox').append(`
                    <div><img src="" alt="Poster Image" class="posterImage feedUser" data-username="${followee.followee_name}"> 
                        <img src="../member/memberProfilePrint?user_id=${followee.followee_id}" alt="Profile Picture" />
                        <span >${followee.followee_name}</span>
                        <img th:src=".../img/영어.jpg" alt="Native Language" />
                        <img th:src=".../img/일본어.jpg" alt="Learning Language" />
                        <button type="button" class="btn btn-primary">팔로우</button>
                    </div>
                    `);
                })
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

function hashtagHighlightAndClick(content) {
    let hashtags = content.match(/#[^\s#]+/g) || [];
    hashtags.forEach(hashtag => {
        const styledHashtag = `<span class="hashtag" style="color: black; cursor: pointer;">${hashtag}</span>`;
        content = content.replace(new RegExp(hashtag, 'g'), styledHashtag);
    });
    return content;
}

function followCheck(){
    let name = $(this).data('username');
    $.ajax({
        url: "followCheck",
        type: "post",
        data: {username : name},
        dataType:'json',
        success:function(result){
            if(result == 0){
                console.log('팔로우');
                followInsert(name);
            }else{
                console.log('언팔로우');
                followDelete(name);
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
}
function followInsert(name){
    $.ajax({
        url:'userFollowInsert',
        type: "post",
        data: {username : name},
        dataType:'json',
        success:function(result){
            console.log(result);
        },
    })
}
function followDelete(name){
    $.ajax({
        url:'userFollowDelete',
        type: "post",
        data: {username : name},
        dataType:'json',
        success:function(result){
            console.log(result);
        },
    })
}