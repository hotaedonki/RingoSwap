
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