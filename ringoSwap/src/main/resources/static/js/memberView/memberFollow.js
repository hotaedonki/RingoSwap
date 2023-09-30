function followerSearch(){
    let nickname = $('.justify-content-center .searchFollower').val();
    console.log("팔로우 닉네임 : ", nickname)
    let userId = $('#userId').val();
    let check = $('#followOpen').val();
    if(check === 'N'){
		if(!memberIdCheck(userId)){
            //자기정보를 보는 것이 아닌이상, 팔로워 공개가 'N'으로 설정된 회원의 팔로워 팔로우를 볼 수 없음
            return;
        }
	}
    $.ajax({
        url: "otherFollowerSearch",
        type: "post",
        data: {nickname : nickname, userId : userId},
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
    let nickname = $('.justify-content-center .searchFollow').val();
    let userId = $('#userId').val();
    let check = $('#followOpen').val();
    if(check === 'N'){
		if(!memberIdCheck(userId)){
            //자기정보를 보는 것이 아닌이상, 팔로워 공개가 'N'으로 설정된 회원의 팔로워 팔로우를 볼 수 없음
            return;
        }
	}
    $.ajax({
        url: "otherFolloweeSearch",
        type: "post",
        data: {nickname : nickname, userId : userId},
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