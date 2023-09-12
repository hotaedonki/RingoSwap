let selectedImages = [];
let saved_feedNum = 0;  //History API를 사용한 피드 번호를 이용한 피드 출력기능에서 피드 번호 전달에 사용하는 전역변수

function initializeEventHandlers() {
    $(".feed-header").click(otherUserProfileButton);
    $(".profile-card").click(goToProfile);
    $(".feed-create-area").blur(collapseWrite);
    $(".post").on('click', createPost);
    $("#imageIcon").on('click', () => $("#imageInput").click());
    $("#imageInput").on('change', handleImageInputChange);

    initializeEmojiArea();
    initializeDocumentClickHandlers();
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
    $(document).on('click', '.reply-delete-button', replyDelete);
    $(document).on('click', '.follower-btn', followerSearch);
    $(document).on('click', '.follow-btn', followeeSearch);
    $(document).on('click', '.insert-nested-reply', insertNestedReply);
    $(document).on('click', '.write-nested-reply', writeNestedReply);
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
    if (!$(event.target).hasClass('bi')) {
        feedDetail.call(this, event);
    }
}

//브라우저에서 뒤로가기 클릭시, History API이 적용된 feedDetail이 아닌 기존 페이지로 이동하는 이벤트
function handlePopState(event) {
    if (event.state) {
        console.log(event.state); // event.state를 기반으로 필요한 작업을 수행합니다.
            						// 예: URL에 따른 페이지 콘텐츠를 로드하거나 특정 동작을 수행합니다.
        history.pushState({ feed_num: event.state.feed_num }, '', `?feed=${event.state.feed_num}`);
        location.reload();
    } else {
		// 페이지가 로드될 때 한 페이지 뒤로 가려면 history.back()을 호출합니다.
        history.replaceState(null, '', ``);
        location.reload();
    }
}

//History API를 사용한 새로고침시에도 피드가 유지되도록 하는 이벤트
function handleWindowLoad() {
    const feedNum = getUrlParam('feed');
    if (feedNum) {
		// 파일 번호가 URL에 있을 경우 해당 텍스트 객체 열기
        saved_feedNum = feedNum;
        console.log('객체 열기');
        feedDetail();
    }
}

$(document).ready(function() {
    feedPrint();
    initializeEventHandlers();
});
