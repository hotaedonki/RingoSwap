function followPrivacy() {
	let type = $(this).hasClass('follower-btn');
	let nickname = $('.nickname').text();
	console.log(type, nickname);
	$.ajax ({
		url: 'followPrivacy'
		, type: 'post'
		, data: {nickname: nickname}
		, success: function(public) {
			console.log(public);
			if(public) {
                if(type) followerSearch();
 				else followeeSearch();
            }else{
                alert('팔로우 비공개 회원입니다.');
            }
		},
		error: function(error) {
			console.log(error);
		}
	})
}

function followerSearch(){
    let search = $('.searchFollower').val();
    userId = $('#userId').val();
    $.ajax({
        url: "otherFollowerSearch",
        type: "post",
        data: {nickname : search, userId : userId},
        dataType:'json',
        success:function(followerList){
            if(followerList){
                $('.followerBox').html('');
                console.log(followerList);
                followerList.forEach(follower => {
                    $('.followerBox').append(`
                    <div>
                    <span class="showOffcanvasWithUserData" data-user-name="${follower.follower_name}">
                        <img src="../member/memberProfilePrint?user_id=${follower.follower_id}" style="width:25px; height:25px; border-radius:12px;" alt="Profile Picture" />
                        <span >${follower.follower_name}</span>
                        <img src="../img/영어.jpg" alt="Native Language" style="width:25px; height:25px; border-radius:12px;" /> <-->
                        <img src="../img/일본어.jpg" alt="Learning Language" style="width:25px; height:25px; border-radius:12px;" />
                    </span>
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
    let search = $('.searchFollow').val();
    userId = $('#userId').val();
    
    // 현재 페이지의 URL 확인
    let currentPage = window.location.href;

    $.ajax({
        url: "otherFolloweeSearch",
        type: "post",
        data: {nickname : search, userId : userId},
        dataType:'json',
        success:function(followeeList){
            console.log(followeeList);
            if(followeeList){
                $('.followBox').html('');
                followeeList.forEach(followee => {
                    console.log(followee);
                    
                    let buttonHtml = '';
                    // currentPage가 "mypage"를 포함하면 버튼 HTML 추가
                    if (currentPage.includes("myPage")) {
                        buttonHtml = `<button type="button" class="btn btn-primary unfollow_button" data-nickname="${followee.followee_name}">Unfollow</button>`;
                    }

                    $('.followBox').append(`
                    <div class="d-flex justify-content-between align-items-center">
	                    <span class="showOffcanvasWithUserData " data-user-name="${followee.followee_name}">
	                        <img src="../member/memberProfilePrint?user_id=${followee.followee_id}" alt="Profile Picture" style="width:25px; height:25px; border-radius:12px;" />
	                        <span >${followee.followee_name}</span>
	                        <img src="../img/영어.jpg" alt="Native Language" style="width:25px; height:25px; border-radius:12px;" /> <-->
	                        <img src="../img/일본어.jpg" alt="Learning Language" style="width:25px; height:25px; border-radius:12px;" />
	                    </span>
	                    ${buttonHtml}
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


function enterEventHandler(){
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

function unFollow() {
	let unfollowName = $(this).data('nickname');
	console.log(unfollowName);
    $.ajax({
        url: "../feed/userFollowDelete",
        type: "post",
        data: {nickname : unfollowName},
        dataType:'json',
        success:function(result){
			$('.unfollow_button').hide();
			$('.btn-close').click();
			memberPrint();
        },
        error: function(error) {
            console.log(error);
        }
    });
}

$(document).ready(function() {   
	$(document).on('click', '.follower-btn', followPrivacy);
	$(document).on('click', '.follow-btn', followPrivacy);
	$(document).on('click', '.unfollow_button', unFollow);
    enterEventHandler();
});