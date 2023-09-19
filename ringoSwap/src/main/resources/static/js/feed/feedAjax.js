let offset = 0;
const limit = 5;

function feedPrint(optionalRes, newLoad = true) {
    let text = $('#searchInput').val();
    console.log(text);
    
    if (optionalRes) {
		console.log("해시태그 클릭 후 feedPrint 반환 값 ", optionalRes)
        renderFeeds(optionalRes, newLoad);
        return;
    }

    $.ajax({
        url: "../feed/feedPrintAll",
        type: "post",
        data: {
            feedArrayType: "default",
            text : text,
            offset: offset,
            limit: limit
        },
        success: function(res) {	
			console.log(res)
            renderFeeds(res, newLoad);
        },
        error: function(error) {
            console.log(error);
        }
    })
}


function renderFeeds(res, newLoad) {
	if(newLoad) {
		$(".feed-display-area .col-12").empty();
	}
	
	console.log("renderFeeds의 res값: ", res)
	let feeds = res.feedList;
	let likeCheck = res.likeCheckMap;
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
                <div class="card-header feed-header">
                    <div class="showOffcanvasWithUserData" data-user-name="${feed.nickname}">
                        <img src="../member/memberProfilePrint?user_id=${feed.user_id}" alt="Poster Image" class="posterImage"> 
                        <span class="feedUser">${feed.nickname}</span>
                    </div>
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
    $('#load-more').remove();
    console.log('완성');
        
    if (feeds.length < limit) {
        $(".feed-display-area .col-12").append(`
            <div id="end-message" style="text-align: center; margin: 10px 0; color: gray;">마지막입니다</div>
        `);
    } else {
        $(".feed-display-area .col-12").append(`
            <button id="load-more" style="display: inline-block; width: 100%; padding: 10px; margin: 10px 0; border: none; background-color: #f0f0f0; cursor: pointer;">더 보기</button>
        `);

        // "더 보기" 버튼 클릭 이벤트 핸들러
        $('#load-more').off('click').on('click', function() {
            offset += limit;  // offset 업데이트
            feedPrint(null, false);  // 추가 피드 로드

            // 로딩 표시 추가
            $(".feed-display-area .col-12").append(`
                <div class="spinner-border text-light" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
            `);
        });
    }

    $(".feed-display-area .col-12 .spinner-border").remove();
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
                    <div class="card-header" style="width: 100%;">
                        <div class="showOffcanvasWithUserData" data-user-name="${detail.feed.nickname}">
                            <img src="../member/memberProfilePrint?user_id=${detail.feed.user_id}" alt="Poster Image" class="posterImage"> 
                            <span class="feedUser">${detail.feed.nickname}</span>
                        </div>
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
	confirm("피드를 삭제하시겠습니까?");	
    $.ajax({
        url: "feedDeleteOne",
        type: "post",
        data: {feed_num : feed_num},
        dataType:'json',
        success:function(res){
			if(res === "0") {
				alert("본인이 작성한 글만 삭제할 수 있습니다.")
			} else { 
			}
			feedPrint();
        },
        error: function(error) {
            console.log(error);
        }
    })
}

function followerSearch(){
    let nickname = $('.justify-content-center .searchFollower').val();
    $.ajax({
        url: "followerSearch",
        type: "post",
        data: {nickname : nickname},
        dataType:'json',
        success:function(followerList){
            if(followerList){
                $('.followerBox').html('');
                console.log(followerList);
                followerList.forEach(follower => {
                    $('.followerBox').append(`
                    <div><img src="" alt="Poster Image" class="posterImage feedUser" data-nickname="${follower.follower_name}"> 
                        <img src="../member/memberProfilePrint?user_id=${follower.follower_id}" style="width:25px; height:25px; border-radius:12px;" alt="Profile Picture" />
                        <span >${follower.follower_name}</span>
                        <img src="../img/영어.jpg" alt="Native Language" style="width:25px; height:25px; border-radius:12px;" />
                        <img src="../img/일본어.jpg" alt="Learning Language" style="width:25px; height:25px; border-radius:12px;" />
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
    let nickname = $('.justify-content-center .searchFollow').val();
    $.ajax({
        url: "followeeSearch",
        type: "post",
        data: {nickname : nickname},
        dataType:'json',
        success:function(followeeList){
            console.log(followeeList);
            if(followeeList){
                $('.followBox').html('');
                followeeList.forEach(followee => {
                    console.log(followee);
                    $('.followBox').append(`
                    <div><img src="" alt="Poster Image" class="posterImage feedUser" data-nickname="${followee.followee_name}"> 
                        <img src="../member/memberProfilePrint?user_id=${followee.followee_id}" alt="Profile Picture" style="width:25px; height:25px; border-radius:12px;" />
                        <span >${followee.followee_name}</span>
                        <img src="../img/영어.jpg" alt="Native Language" style="width:25px; height:25px; border-radius:12px;" />
                        <img src="../img/일본어.jpg" alt="Learning Language" style="width:25px; height:25px; border-radius:12px;" />
                        <button type="button" class="btn btn-primary">팔로우</button>
                    </div>
                    `);
                })
                $('.followBox').show();
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

function followInsert(){
    let name = document.getElementById('nickname').textContent;
    $.ajax({
        url:'userFollowInsert',
        type: "post",
        data: {nickname : name},
        dataType:'json',
        success:function(result){
            console.log('팔');
            console.log(result);
            if(result === -1){
                alert('자기자신을 팔로우할 수 없습니다.');
            }
            memberPrint();
        },
        error:function(e){
            console.log('eee');
        }
    })
}
function followDelete(){
    let name = document.getElementById('nickname').textContent;
    console.log(name);
    $.ajax({
        url:'userFollowDelete',
        type: "post",
        data: {nickname : name},
        dataType:'json',
        success:function(result){
            console.log('언팔');
            console.log(result);
            if(result === -1){
                alert('자기자신을 팔로우할 수 없습니다.');
            }
            memberPrint();
        },
        error:function(e){
            console.log('eee');
        }
    })
}
