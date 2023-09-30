function followPrivacy() {
	let type = $(this).hasClass('follower-btn');
	
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
			}	
		},
		error: function(error) {
			console.log(error);
		}
	})
}

function followerSearch(){
    $.ajax({
        url: "../feed/followerSearch",
        type: "post",
        data: {nickname : nickname},
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
                        <img src="../img/영어.jpg" alt="Native Language" style="width:25px; height:25px; border-radius:12px;" />
                        <img src="../img/일본어.jpg" alt="Learning Language" style="width:25px; height:25px; border-radius:12px;" />
                    </span>
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
    $.ajax({
        url: "../feed/followeeSearch",
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
                    <div class="d-flex justify-content-between align-items-center">
	                    <span class="showOffcanvasWithUserData " data-user-name="${followee.followee_name}">
	                        <img src="../member/memberProfilePrint?user_id=${followee.followee_id}" alt="Profile Picture" style="width:25px; height:25px; border-radius:12px;" />
	                        <span >${followee.followee_name}</span>
	                        <img src="../img/영어.jpg" alt="Native Language" style="width:25px; height:25px; border-radius:12px;" /> <-->
	                        <img src="../img/일본어.jpg" alt="Learning Language" style="width:25px; height:25px; border-radius:12px;" />
	                    </span>
	                        <button type="button" class="btn btn-primary unfollow_button" data-nickname="${followee.followee_name}">Unfollow</button>
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

    
    
$(document).ready(function() {   
	$(document).on('click', '.follower-btn', followPrivacy);
	$(document).on('click', '.follow-btn', followPrivacy);
});