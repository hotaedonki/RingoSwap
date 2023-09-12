function replyInsert() {
	const replyContent = $(this).closest('.replyMargin').find(".replyContent").val();
    const feedNum = $(this).closest('.feed-card').data('feed-num');
	console.log(replyContent, feedNum);
	
	$.ajax({
		url: "replyInsert",
		type: "post",
		data: { feed_num: feedNum, contents: replyContent, parent_reply_num: -1},
		success: function(feedNum) {
			$(".replyContent").val('');
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
        data: { feed_num: feedNum },
        success: function(replyMap) {
            $(".replyPrint").empty();
			
			let replys = replyMap.replyList;
			let replyLike = replyMap.likeCheckMap;
            // 댓글과 답글을 분리
            let comments = replys.filter(reply => reply.parent_reply_num === -1);
            let nestedReplies = replys.filter(reply => reply.parent_reply_num !== -1);

            comments.forEach(comment => {
				let likeCheck = replyLike[comment.reply_num];
				let likeButtonClass = likeCheck === 1 ? "bi-heart-fill" : "bi-heart"
				
				let current_date = new Date();
				let comment_date = new Date(comment.inputdate);
				let time_diff_str = timeDifference(current_date, comment_date);
                let commentElement = $(`
                    <div class="comment-list replyMargin" data-reply-id="${comment.reply_num}">
			            <div class="comment-item">
			                <div class="row">
			                	<div class="row comment-1 d-flex align-items-center" style="margin-bottom: 0px;">
				                	<div class="col-1 goToOtherProfile">
				                        <img src="../member/memberProfilePrint?user_id=${comment.user_id}" alt="User Photo" class="user-photo-reply"> 
				                    </div>
				                    <div class="col-3 goToOtherProfile>
				                        <span class="user-id">${comment.username}</span>
				                    </div>
				                    <div class="col-7"> 
				                        <span class="comment-text nestedReply">${comment.contents}</span>			                        
				                    </div>
    				                <div class="col-1 d-flex justify-content-end">
				                        <i class="bi ${likeButtonClass} unLike replyLike" data-reply-num="${comment.reply_num}"></i>                
				                	</div>
			                    </div>
			                    <div class="row comment-2 g-0" style="margin: 0;  font-size:18px;">
				                    <div class="col-2">
				                    	<small class="comment-date">${time_diff_str}</small>
				                    </div>
				                    <div class="col-2">
				                        <small>좋아요 <span class="like-count" data-reply-num="${comment.reply_num}">${comment.like_count}</span>개</small>
				                    </div>
				                    <div class="col-2">
				                        <small class="write-nested-reply" data-username-id="${comment.username}">답글달기</small>
				                    </div>
				                    <div class="col-2">
				                        <button class="btn btn-primary reply-delete-btn btn-sm ml-2" data-reply-num="${comment.reply_num}">삭제</button>
				                    </div>
				                </div>
			                </div>
			            </div>
			        </div>
			    `);
			    
			    let viewNestedReplyDiv = $('<div class="view-nested-reply"></div>');
    			commentElement.append(viewNestedReplyDiv);

                // 해당 댓글에 대한 답글을 찾아 출력
                let commentNestedReplies = nestedReplies.filter(reply => reply.parent_reply_num === comment.reply_num);
               
                if (commentNestedReplies.length > 0) {
			        let viewRepliesButton = $('<button>')
			            .text('답글 보기')
			            .on('click', function() {
			                $(this).closest('.comment-list').find('.nested-reply-list').toggle();
			            });
			
			        viewNestedReplyDiv.append(viewRepliesButton);
			    }
			    
                commentNestedReplies.forEach(nestedReply => {
				    commentElement.append(`
				        <div class="comment-list replyMargin nested-reply-list" style="display: none; margin-left: 30px;" data-reply-id="${nestedReply.reply_num}">
				            <div class="comment-item">
				                <div class="row">
				                    <div class="col-7 d-flex align-items-center">
				                        ${nestedReply.contents}
				                    </div>
				                    <div class="col-5 d-flex justify-content-end">
				                        <i class="bi bi-heart unLike replyLike"></i>
				                        <button class="btn btn-primary reply-btn ml-2 nestedReply" style="margin-right: 5px;">답글</button>
				                        <button class="btn btn-primary reply-delete-btn ml-2" data-reply-num="${comment.reply_num}">삭제</button>
				                    </div>
				                </div>
				                <div class="row">
				                	<div class="col-9">
				                    	<input type="text" class="form-control" placeholder="답글을 입력하세요..." />
			                   		</div>
			                   		<div class="col-3 d-flex justify-content-end"> 	
			                   		 	<button class="btn btn-primary insertNestedReply">작성</button>
				                	</div>
				                </div>
				                <div class="row view-nested-reply">
				                </div>
				            </div>
				        </div>
				    `);
				});


                $(".replyPrint").append(commentElement);
            });
        },
        error: function(error) {
            console.error(error);
        }
    });
}


function insertNestedReply() {
    const replyContent = $(this).closest('.comment-item').find('input').val();
    const feedNum = $(this).closest('.card.feed-card').data('feed-num');
    const parentReplyNum = $(this).closest('.comment-list').data('reply-id');
    console.log(replyContent, feedNum, parentReplyNum);
    
    $.ajax({
        url: "replyInsert",
        type: "post",
        data: { feed_num: feedNum, contents: replyContent, parent_reply_num: parentReplyNum },
        success: function() {
			$(".replyContent").val('');
            replyPrint(feedNum);
        },
        error: function(error) {
            console.error(error);
        }
    });
}

function writeNestedReply() {
	const TagUserName = $(this).data('username-id');
	$(".replyContent").val("@" + TagUserName + " ")
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

function replyDelete(){
    let reply_num = $(this).data('reply-num');
    let feed_num = $('.feed-card').data('feed-num');
    console.log(reply_num);
	
    $.ajax({
        url: "replyDeleteOne",
        type: "post",
        data: {reply_num : reply_num},
        success:function(res){
			if(res) {
				alert(res);
				console.log("본인이 작성한 글만 삭제할 수 있습니다.");
			} 
			replyPrint(feed_num);
        },
        error: function(error) {
            console.log(error);
        }
    })
}