function replyInsert() {
	const replyContent = $(this).closest('.replyMargin').find(".replyContent").val();
    const feedNum = $(this).closest('.feed-card').data('feed-num');
	console.log(replyContent, feedNum);
	
	const hashtags = replyContent.match(/#\w+/g) || [];
    console.log(hashtags);
	
	$.ajax({
		url: "replyInsert",
		type: "post",
		data: { feed_num: feedNum, contents: replyContent, parent_reply_num: -1, hashtags: JSON.stringify(hashtags)},
		success: function(feedNum) {
			$(".replyContent").val('');
			replyPrint(feedNum);
			
			hashtags.forEach(function(tag) {
                $(`.replyContent:contains("${tag}")`).html(function(_, html) {
                    return html.replace(tag, `<a href="search?tag=${tag.slice(1)}">${tag}</a>`);
                });
            });
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
			
			console.log("comments : ", comments);
			console.log("nestedReplies : ", nestedReplies);
			
            comments.forEach(comment => {
				let likeCheck = replyLike[comment.reply_num];
				let likeButtonClass = likeCheck === 1 ? "bi-heart-fill" : "bi-heart"
				
				let current_date = new Date();
				let comment_date = new Date(comment.inputdate);
				let time_diff_str = timeDifference(current_date, comment_date);
                let commentElement = $(`
                    <div class="comment-list replyMargin" data-reply-id="${comment.reply_num}">
			            <div class="comment-item">
			                <div class="row comment-area">
			                	<div class="row comment-1 d-flex align-items-center" style="margin-bottom: 0px;">
				                	<div class="col-1 goToOtherProfile" data-user-name="${comment.username}">
				                        <img src="../member/memberProfilePrint?user_id=${comment.user_id}" alt="User Photo" class="user-photo-reply"> 
				                    </div>
				                    <div class="col-3 goToOtherProfile" data-user-name="${comment.username}">
				                        <span class="user-id">${comment.username}</span>
				                    </div>
				                    <div class="col-7"> 
				                        <span class="comment-text nestedReply">${comment.contents}</span>			                        
				                    </div>
    				                <div class="col-1 d-flex justify-content-end">
				                        <i class="bi ${likeButtonClass} unLike replyLike" data-reply-num="${comment.reply_num}"></i>                
				                	</div>
			                    </div>
			                    <div class="row comment-2 g-0">
				                    <div class="col-2" style="margin-left: 13px;">
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

			                <div class="row nested-reply-area" style="margin-left: 30px;" data-reply-id="${comment.reply_num}">
			                	<div class="row nested-reply" style="display: none;">
				                	<div class="col-7 d-flex align-items-center nested-reply-contents">
					                        
					                </div>
					                    <div class="col-5 d-flex justify-content-end">
					                        <i class="bi bi-heart unLike replyLike"></i>
					                        <button class="btn btn-primary reply-btn ml-2 nestedReply" style="margin-right: 5px;">답글</button>
					                        <button class="btn btn-primary reply-delete-btn ml-2">삭제</button>
					                    </div>
					                </div>
				                </div>
				                <div class="row show-nested-reply-form" style="display: none;">
				                	<div class="col-9">
				                    	<input type="text" class="form-control follow-search-input" placeholder="답글을 입력하세요..." />
			                   		</div>
			                   		<div class="col-3 d-flex justify-content-end"> 	
			                   		 	<button class="btn btn-primary insert-nested-reply">작성</button>
				                	</div>
				                </div>
				                <div class="row view-nested-reply">
				                    <!-- 답글보기 버튼 -->
				                </div>
				                <div class="row nested-reply-list">
				                	<!-- 대댓글 영역 -->
				                </div>
			                </div>
			            </div>
			        </div>
			    `);
				
				let commentNestedReplies = nestedReplies.filter(reply => reply.parent_reply_num === comment.reply_num);
                let nestedReplyToggle = '';
                console.log("특정 댓글의 답글내역 : ", commentNestedReplies);
                
                if(commentNestedReplies.length > 0) {	
					nestedReplyToggle = $(`
							<span class="btn btn-link btn-sm view-nested-reply-button col-6" data-reply-id="${comment.reply_num}">---------------답글 보기 (${commentNestedReplies.length}) -------------- </span>
							`)
					commentElement.find(".view-nested-reply").append(nestedReplyToggle);	
						
	                commentNestedReplies.forEach(nestedReply => {
						console.log("nestedReply 각 개체의 값: ", `${nestedReply.reply_num}`)					
				        let nestedReplyElement = $(`
				            <div class="nested-reply-item" data-reply-id="${comment.reply_num}" data-user-id="${comment.user_id}" style="display: none; margin-left: 30px;">
				            	<div class="row nested-reply-1 d-flex align-items-center">
				            		<div class="col-1 nested-goToOtherProfile">
				            			<img src="../member/memberProfilePrint?user_id=${comment.user_id}" alt="User Photo" class="user-photo-reply">
					            	</div>
					            	<div class="col-2 nested-goToOtherProfile">
						                <span class="nested-reply-user">${nestedReply.username}</span>
					            	</div>
					            	<div class="col-6">
					            		<span class="nested-reply-text">${nestedReply.contents}</span>
					            	</div>
					            	<div class="col-2">
				                        <i class="bi bi-heart unLike replyLike" data-reply-num="${nestedReply.reply_num}"></i>                
				                	</div> 
					            </div>
					            <div class="row nested-reply-2 g-0">
					            	<div class="col-2">
				                    	<small class="nested-reply-date">${time_diff_str}</small>
				                    </div>
				                    <div class="col-2">
				                        <small>좋아요 <span class="like-count" data-reply-num="${nestedReply.reply_num}">${nestedReply.like_count}</span>개</small>
				                    </div>
				                    <div class="col-2">
				                        <small class="write-nested-reply" data-username-id="${nestedReply.username}">답글달기</small>
				                    </div>
				                    <div class="col-2">
				                        <button class="btn btn-primary reply-delete-btn btn-sm ml-2" data-reply-num="${nestedReply.reply_num}">삭제</button>
					            	</div>
					            </div>
				            </div>
				        `);
				
				        commentElement.find(".nested-reply-list").append(nestedReplyElement);
				    });
				    
				    nestedReplyToggle.on('click', function(){
				        let commentId = $(this).data('reply-id');
				        console.log("답글 보기를 눌렀을 때 해당 댓글의 reply-id : ", commentId)
				        commentElement.find(`.nested-reply-item[data-reply-id="${commentId}"]`).toggle();
    					});
				    }	
				    
                $(".replyPrint").append(commentElement);
                
                $('.replyContent').each(function() {
			        const content = $(this).text();
			        const hashtags = content.match(/#\w+/g) || [];
			        hashtags.forEach(function(tag) {
			            $(this).html(function(_, html) {
			                return html.replace(tag, `<a href="search?tag=${encodeURIComponent(tag.slice(1))}">${tag}</a>`);
			            });
			        }, this);
			    });
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
    let parentReplyNum = $(this).closest('.comment-item').find('input').data('parent-reply-num');

    //@뒤에 붙어있는 유저 이름 값을 배열로 가져옴.
    const mentionedUsers = replyContent.match(/@[a-zA-Z0-9_]+/g);
    
    console.log(replyContent, feedNum, parentReplyNum, mentionedUsers);
       
    $.ajax({
        url: "replyInsert",
        type: "post",
        data: { feed_num: feedNum, contents: replyContent, parent_reply_num: parentReplyNum, mentionedUsers: mentionedUsers },
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
    const parentReplyNum = $(this).closest('.comment-list').data('reply-id');
    
    console.log("writeNestedReply : ", TagUserName, parentReplyNum)
    
    $(this).closest('.comment-list').find(".show-nested-reply-form").toggle();
    $(this).closest('.comment-list').find(".follow-search-input")
    	.val("@" + TagUserName + " ")
       .data('parent-reply-num', parentReplyNum); // Set the parent reply num in data attribute
}


//댓글 좋아요 기능
function clickLikeReply() {
    const replyNum = $(this).data('reply-num');
    const buttonElement = $(this); // $(this) 참조를 저장
	
	console.log("댓글 좋아요 기능: ", replyNum, buttonElement)
	
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

function followSearchInput() {
	const inputVal = $(this).val();
	const atIndex = inputVal.lastIndexOf('@');
	
	if (atIndex !== -1) {
		const username = inputVal.substring(atIndex + 1);
		
		if (username.length > 0) {
			$.ajax({
				url: 'followeeSearch'
				, type: 'post'
				, data: {username: username}
				, success: function(followee) {
					createDropdownList(followee)
				},
				error: function(error) {
					console.log(error);
				}
			})
		}
	} 		
}

function createDropdownList(followee) {
    const dropdownMenu = $('<div class="dropdown-menu"></div>');

    followee.forEach(item => {
        const dropdownItem = $(`<a class="dropdown-item" href="#">${item.followee_name}</a>`);
        dropdownItem.on('click', function() {
            const username = $(this).text();
            const inputField = $('.your-input-class');
            const currentValue = inputField.val();
            const newValue = currentValue.replace(/@[a-zA-Z0-9_]+$/, '@' + username);
            inputField.val(newValue);
        });
        dropdownMenu.append(dropdownItem);
    });

    $('.follow-search-input').after(dropdownMenu);
    dropdownMenu.show(); 
}