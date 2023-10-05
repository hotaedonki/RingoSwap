
function createChatroomThumbnail(chatroom_num, title, inputdate, message)
{
	// chatlist의 div 요소 접근
    let $chatlist = $('.chatlist');

    // 새로운 block div 요소를 생성
    let $blockDiv = $('<div>').addClass('block');
    
    // details, listHead div 요소를 생성 및 내용 추가
    let current_date = new Date();
    let chatroom_date = new Date(inputdate);
    let time_diff_str = timeDifferenceInChat(current_date, chatroom_date)
    
    let $detailsDiv = $('<div>').addClass('details').append(
        $('<div>').addClass('listHead').append(
            $('<input>').attr('type', 'hidden').val(chatroom_num),
            $('<h5>').addClass('chatroom-title').text(title),
            $('<p>').addClass('time chatroom-time').text(time_diff_str)
        ),
        $('<div>').addClass('message_p').append(
            $('<p>').addClass('chatroom-message').text(message)
        )
    );
    // blockDiv에 detailsDiv 추가 및 chatlist에 blockDiv 추가
    $blockDiv.append($detailsDiv).appendTo($chatlist);
	$blockDiv.append('<hr>');
    // blockDiv 클릭 이벤트 리스너 추가
    $blockDiv.on('click', function() {
        moveToChatroom(chatroom_num);
    });
    
    //메시지 보낸사람 닉네임받아오기
}

function createDMChatroomThumbnail(chatroom_num, title, inputdate, message)
{
	// chatlist의 div 요소 접근
    let $chatlist = $('.chatlist');

    // 새로운 block div 요소를 생성
    let $blockDiv = $('<div>').addClass('block');
    
    // details, listHead div 요소를 생성 및 내용 추가
    let current_date = new Date();
    let chatroom_date = new Date(inputdate);
    let time_diff_str = timeDifferenceInChat(current_date, chatroom_date)
    
    let $detailsDiv = $('<div>').addClass('details').append(
        $('<div>').addClass('listHead').append(
            $('<input>').attr('type', 'hidden').val(chatroom_num),
            $('<h5>').addClass('chatroom-title').text(title),
            $('<p>').addClass('time chatroom-time').text(time_diff_str)
        ),
        $('<div>').addClass('message_p').append(
            $('<p>').addClass('chatroom-message').text(message)
        )
    );
    // blockDiv에 detailsDiv 추가 및 chatlist에 blockDiv 추가
    $blockDiv.append($detailsDiv).appendTo($chatlist);
	$blockDiv.append('<hr>');
    // blockDiv 클릭 이벤트 리스너 추가
    $blockDiv.on('click', function() {
        moveToDMChatroom(chatroom_num);
    });
    
    //메시지 보낸사람 닉네임받아오기
}


function timeDifferenceInChat(current, previous) {
    const msPerMinute = 60 * 1000;
    const msPerHour = msPerMinute * 60;
    const msPerDay = msPerHour * 24;
    const msPerMonth = msPerDay * 30;
    const msPerYear = msPerDay * 365;
    const elapsed = current - previous;
    
	if (elapsed < msPerHour) {
        return Math.floor(elapsed/msPerMinute) + '분 전';   
    }
    else if (elapsed < msPerDay) {
        return Math.floor(elapsed/msPerHour ) + '시간 전';   
    }
    else if (elapsed < msPerMonth) {
        return Math.floor(elapsed/msPerDay) + '일 전';   
    }
    else if (elapsed < msPerYear) {
        return Math.floor(elapsed/msPerMonth) + '달 전';   
    }
    else {
        return Math.floor(elapsed/msPerYear) + '년 전';   
    }
}

const offcanvaslanguage = {
   "한국어": "../img/한국어.jpg",
   "일본어": "../img/일본어.jpg",
   "영어": "../img/영어.jpg"
};

let offcanvasName;
function showOffcanvasWithUserDataChat() {
    const nickname = $(this).data('user-name');
    offcanvasName = nickname;
    const offcanvsElement = document.getElementById('offcanvasWithBothOptions-chat');
    const offcanvas = new bootstrap.Offcanvas(offcanvsElement);

    console.log(nickname);

    $.ajax({
        url: '../feed/showOffcanvasWithUserData',
        type: 'post',
        data: { nickname: nickname },
        success: function(userInfo) {
            document.getElementById('nickname-chat').textContent = userInfo.nickname;
            $('#nickname-chat').attr('data-nickname', userInfo.nickname);
            document.getElementById('original_profile-chat').src = "../member/memberProfilePrint?user_id=" + userInfo.user_id;
            document.getElementById('target_lang_img-chat').src = userInfo.target_lang;
            document.getElementById('native_lang_img-chat').src = userInfo.native_lang;
            document.getElementById('introduction-chat').textContent = userInfo.introduction ? userInfo.introduction : "자기소개 없음";

            let native = printLanguage(userInfo.native_lang);
            let target = printLanguage(userInfo.target_lang);

            $('#native_lang_img-chat').attr('src', native);
            $('#target_lang_img-chat').attr('src', target);
			
			$('.goToOtherProfile-chat').attr('data-nickname', userInfo.nickname);
            followCheck(nickname);
            offcanvas.show();
        },
        error: function(error) {
            console.error(error);
        }
    });
}

function printLanguage(lang){
   if(lang === 'ko'){
       lang = offcanvaslanguage["한국어"];
   }else if(lang === 'ja'){
       lang = offcanvaslanguage["일본어"];
   }else if(lang === 'en'){
       lang = offcanvaslanguage["영어"];
   }
   return lang;
}

function goToOtherProfileChat(){
	let nickname = $(this).find('[data-nickname]').data('nickname');
	
	const url = `../member/otherPage?nickname=${encodeURIComponent(nickname)}`;
	window.location.href = url;

}

function followCheck(nickname){
	//해당 회원을 팔로우 했는지 여부를 체크하여, 오프캔버스의 팔로우 관련 버튼 중 해당하는 기능의 버튼을 출력하는 함수
    $.ajax({
        url: "../feed/followCheck",
        type: "post",
        data: {nickname : nickname},
        dataType:'json',
        success:function(result){
            if(result == 0){
                console.log('팔로우');
				$('.follow_button-chat').show();			//버튼을 출력함
				$('.unfollow_button-chat').hide();		//버튼을 숨김
            }else{
                console.log('언팔로우');
				$('.follow_button-chat').hide();			//버튼을 숨김
				$('.unfollow_button-chat').show();		//버튼을 출력함
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function followInsert(){
    $.ajax({
        url:'../feed/userFollowInsert',
        type: "post",
        data: {nickname : offcanvasName},
        dataType:'json',
        success:function(result){
            console.log('팔');
            console.log(result);
            if(result === -1){
                alert('자기자신을 팔로우할 수 없습니다.');
            }
            $('.btn-close').click();
        },
        error:function(e){
            console.log('eee');
        }
    })
}
function followDelete(){
    if(name === '') {
		name = $(this).data('nickname');
	}
	
    console.log(name);
    $.ajax({
        url:'../feed/userFollowDelete',
        type: "post",
        data: {nickname : offcanvasName},
        dataType:'json',
        success:function(result){
            console.log('언팔');
            console.log(result);
            if(result === -1){
                alert('자기자신을 팔로우할 수 없습니다.');
            }
            $('.btn-close').click();
        },
        error:function(e){
            console.log('eee');
        }
    })
}

function toggleChatList() {
    $('.hide-chat-list').toggle();

    if (!$('.hide-chat-list').is(':visible')) {
        $('.chat-box').css({
            'border-top-left-radius': '6px',
            'border-bottom-left-radius': '6px'
        }).addClass('ms-3');
    } else {
        $('.chat-box').css({
            'border-top-left-radius': '0',
            'border-bottom-left-radius': '0'
        }).removeClass('ms-3');
    }

    adjustChatBoxAreaWidth();
}



function toggleParticipants() {
    $('.hide-participants').toggle();

    if (!$('.hide-participants').is(':visible')) {
        $('.chat-box').css({
			'border-top-right-radius': '6px',
            'border-bottom-right-radius': '6px'
		}); 
    } else {
        $('.chat-box').css({
			'border-top-right-radius': '0px',
            'border-bottom-right-radius': '0px'
		});
    }

    adjustChatBoxAreaWidth();
}


function adjustChatBoxAreaWidth() {
    let chatListVisible = $('.hide-chat-list').is(':visible');
    let participantsVisible = $('.hide-participants').is(':visible');

    // 초기화: 기본적으로 모든 클래스를 제거
    $('.chat-box-area').removeClass('col-md-7 col-md-9 col-md-10 col-md-12');

    // 1. chat-box-area는 기본적으로 col-md-7이다.
    if (chatListVisible && participantsVisible) {
        $('.chat-box-area').addClass('col-md-7');
    }
    // 2. hide-chat-list가 없으면 chat-box-area는 col-md-10이다.
    else if (!chatListVisible && participantsVisible) {
        $('.chat-box-area').addClass('col-md-10');
    }
    // 3. hide-participants가 없으면 chat-box-area는 col-md-9이다.
    else if (chatListVisible && !participantsVisible) {
        $('.chat-box-area').addClass('col-md-9');
    }
    // 4. 둘 다 없으면 chat-box-area는 col-md-12이다.
    else if (!chatListVisible && !participantsVisible) {
        $('.chat-box-area').addClass('col-md-12');
    }
}

function showChatRoomCapacity(chatlinkCount){
    let count = $('.participant-nickname').length;
    $('.capasity-count').text(`
        (${count}/10)
    `);
}

$(document).ready(function() {
	$(document).on('click', '.showOffcanvasWithUserData', showOffcanvasWithUserDataChat);
	$(document).on('click', '.goToOtherProfile-chat', goToOtherProfileChat);
	$(document).on('click', '.follow_button-chat', followInsert);
    $(document).on('click', '.unfollow_button-chat', followDelete);
    $(document).on('click', '#toggleChatList', toggleChatList);
    $(document).on('click', '#toggleParticipants', toggleParticipants);
    showChatRoomCapacity();
});