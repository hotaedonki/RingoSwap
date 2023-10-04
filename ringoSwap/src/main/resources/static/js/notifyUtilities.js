

function notificationPrint(){

	$.ajax({
        url:'/ringo/feed/feedNotifyPrint',
        type:"post",
        dataType:'json',
        success:function(notifyList){
            $('.notification-card').append(`
            <ul class="list-group list-group-flush">
            `);
            for(let i=0;i<Math.min(10, notifyList.length);i++){
                let notify = notifyList[i];
                if(notify.notify_type === 'like'){
                    $('.notification-card').append(`
                    <li class="list-group-item">
                        <span class="otherUser showOffcanvasWithUserData" data-user-name="${notify.nickname}">${notify.nickname}</span>님이
                        <span class="myFeedNotify" data-feed-num="${notify.feed_num}">당신의 피드</span>에
                        좋아요를 남겼습니다.
                    </li>
                    `);
                }else{
                    $('.notification-card').append(`
                    <li class="list-group-item">
                        <span class="otherUser showOffcanvasWithUserData" data-user-name="${notify.nickname}">${notify.nickname}</span>님이
                        <span class="myFeedNotify" data-feed-num="${notify.feed_num}">당신의 피드</span>에
                        댓글을 남겼습니다.
                    </li>
                    `);
                }
            }
            let cnt = Math.max(0, notifyList.length-10);

            if(cnt != 0){
                $('.notification-card').append(`
                <li class="list-group-item">
                    이하 +${cnt}개의 알림이 존재합니다.
                </li>
                `);

            }
            $('.notification-card').append(`
            </ul>
            `);
        },
        error:function(e){
            notify.error(e);
        }
    })
}

$(document).ready(function(){
    notificationPrint();
    $(document).on('click', '.myFeedNotify', feedDetail);
});