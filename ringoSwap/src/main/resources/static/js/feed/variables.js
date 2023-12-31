let selectedImages = [];
let saved_feedNum = 0;  //History API를 사용한 피드 번호를 이용한 피드 출력기능에서 피드 번호 전달에 사용하는 전역변수
let previousUrl = null;  //handlePopState에서 사용할 뒤로가기/앞으로가기 판별용 전역변수. 현재 주소를 담는다.

function initializeEventHandlers() {
    $(".profile-card").click(goToProfile);
    $(".feed-create-area").blur(collapseWrite);
    $(".post").on('click', createPost);
    $("#imageIcon").on('click', () => $("#imageInput").click());
    $("#imageInput").on('change', handleImageInputChange);

    initializeEmojiArea();
    initializeDocumentClickHandlers();
    initializeEnterEventHandler();
    initializeWindowEventHandlers();
}

function initializeEmojiArea() {
    let eA = $("#chatInput").emojioneArea({ filtersPosition: "bottom", pickerPosition: "bottom", });

    eA[0].emojioneArea.on("focus", () => {
        $(".emojionearea-editor").css("height", "150px");
        $(".feed-create-area .icons").show();
        $(".feed-create-area .post").show();
    });
}

function initializeDocumentClickHandlers() {
    $(document).on('click', '.insertReply', replyInsert);
    $(document).on('click', '.like-button', clickLikeFeed);
    $(document).on('click', '.replyLike', clickLikeReply);
    $(document).on('click', '.collapseFeed', handleCollapseFeedClick);
    $(document).on('click', '#backToFeed', returnFeedMain);
    $(document).on('click', '.feed-delete-button', feedDelete);
    $(document).on('click', '.reply-delete-btn', replyDelete);
    $(document).on('input', '.searchFollower', followerSearch);
    $(document).on('input', '.searchFollow', followeeSearch);
    $(document).on('click', '.insert-nested-reply', insertNestedReply);
    $(document).on('click', '.write-nested-reply', writeNestedReply);
    $(document).on('input', '.follow-search-input', followSearchInput);
    $(document).on('click', '.hashtag', hashtagSearch);
    $(document).on('input', '#searchInput', feedSearch);
    $(document).on('click', '.showOffcanvasWithUserData', showOffcanvasWithUserData);
    $(document).on('click', '.follow_button', followInsert);
    $(document).on('click', '.unfollow_button', followDelete);
    $(document).on('click', '.goToOtherProfile', goToOtherProfile);
    $(document).on('click', '.translate', feedTranslate);
    $(document).on('show.bs.modal', '.followModalClass', followeeSearch);
    $(document).on('show.bs.modal', '.followerModalClass', followerSearch);
    $(document).on('click', '#message_button', sendDM);
}

function initializeEnterEventHandler(){
    $(document).on('keyup', '.searchFollower', function(event){
		if(event.which == 13){
			followerSearch();
		}

	});
	$(document).on('keyup', '.searchFollow', function(event){
		if(event.which == 13){
			followeeSearch();
		}

	});
}

function initializeWindowEventHandlers() {
    window.addEventListener('popstate', handlePopState);
    window.addEventListener('load', handleWindowLoad);
}

//선택된 이미지 파일을 배열에 저장 후 미리보기 생성.
function handleImageInputChange(event) {
    const files = event.target.files;

    for (let i = 0; i < files.length; i++) {
        selectedImages.push(files[i]);

        const imgElement = document.createElement('img');
        imgElement.src = URL.createObjectURL(files[i]);
        imgElement.classList.add('image-preview');
        $("#imagePreviewContainer").append(imgElement);
    }
}

function handleCollapseFeedClick(event) {
    // 누르지 않아야 하는 요소 목록을 정의합니다.
    const nonClickableSelectors = [
        '.like-button', 
        '.hashtag', 
        '.posterImage', 
        '.translate'
    ];

    // 클릭된 요소가 nonClickableSelectors 중 하나에 해당하는지 확인합니다.
    const clickedOnNonClickableElement = nonClickableSelectors.some(
        selector => $(event.target).closest(selector).length > 0
    );

    // 클릭된 요소가 nonClickableSelectors 중 하나에 해당하면 feedDetail 함수를 호출하지 않습니다.
    if (!clickedOnNonClickableElement && !($(event.target).hasClass('like-button') || $(event.target).hasClass('translate'))) {
	    feedDetail.call(this, event);
	}
	
}

//브라우저에서 뒤로가기 클릭시, History API이 적용된 feedDetail이 아닌 기존 페이지로 이동하는 이벤트
function handlePopState(event) {
    let newUrl = window.location.href;

    if(previousUrl === newUrl){
        return; //현재 url과 이전 url이 동일할경우, 아무 작업도 수행하지 않도록 리턴
    }
    
    if (event.state && event.state.feed_num) {
        // 이전 URL이 존재하고 현재 URL과 다른 경우, 앞으로 가기 작업을 실행
        console.log('앞으로 가기');
        saved_feedNum = event.state.feed_num;
        feedDetail();
    } else {
        // 이전 URL이 없으면서 현재 URL과 다른 경우, 뒤로가기 작업
        console.log('뒤로 가기');
        feedPrint();
    }
    
    previousUrl = newUrl; // 현재 URL을 이전 URL로 저장합니다.
}

//History API를 사용한 새로고침시에도 피드가 유지되도록 하는 이벤트
function handleWindowLoad() {
	console.log("히스토리api")
    const feedNum = getUrlParam('feed');
    if (feedNum) {
      // 파일 번호가 URL에 있을 경우 해당 텍스트 객체 열기
        saved_feedNum = feedNum;
        console.log('객체 열기');
        feedDetail();
        
    }
}

function hashtagSearch() {
	const hashtag = $(this).text().substring(1);
	console.log("해시태그 클릭: ", hashtag);
	
	$.ajax({
		url: 'feedTagSearch'
		, type: 'get'
		, data: {tag_name: hashtag, feedArrayType: 'default'}
		, dataType: 'json'
		, success: function(res) {
			console.log("해시태그 클릭 성공", res)
			feedPrint(res)
		},
		error: function(error) {
			console.error(error);
		}
	});
}

$(document).ready(function() {
    feedPrint();
    friendPrint();
    initializeEventHandlers();   
});