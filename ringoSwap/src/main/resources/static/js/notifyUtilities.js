

function notificationPrint(){

	let feed = {
		user_num: myUserNum, // 사용자 번호
		feed_num : feedNum
	};

	stompClient.subscribe('sub/feed/feedMainNotify' + nickName, {}, JSON.stringify(chatCommon));

	// 입장, 퇴장 관련 메시지를 받는 이벤트
	stompClient.subscribe('/sub/feed/feedMainNotify/state/' + nickName, onNotifyForState)
	// 메시지를 받는 이벤트 => /sub/chat/openChatRoom/message/채팅방번호 로 구독한다
	stompClient.subscribe('/sub/feed/feedMainNotify/state/' + nickName, onNotifyReceived);

	// 자신이 참가한 채팅방의 번호들을 자신의 고유번호로 가져오는 것을 요청
	stompClient.send('/pub/feed/feedMainNotify/loadFeedLike/' + myUserNum, {}, myUserNum);
	// 자신이 참가한 채팅방의 번호들을 자신의 고유번호로 가져오는 것을 받기 위한 이벤트 연결
	stompClient.subscribe('/sub/feed/feedMainNotify/loadFeedLike/' + myUserNum, loadChatRoomNumsByUserNum);
	
	// 검색창에 방 제목을 입력할 시 결과를 받기 위해 이벤트 연결
	stompClient.subscribe('/sub/feed/feedMainNotify/searchByFeedNum/' + myUserNum, searchResultByTitle);


	let num = $('.notification-card').append();
}
//알림 입장, 퇴장시마다 실행하는 함수?
function onNotifyForState(notify){
    console.log(notify);
}
//알림을 받는 이벤트
function onNotifyReceived(notify){
    // message의 body 속성을 가져와서 파싱
    const bodyString = notify.body;
    console.log(bodyString);
    if (bodyString) {
        try {
            const bodyObj = JSON.parse(bodyString);
            const messageValue = bodyObj.notify;
            const type = bodyObj.type;
            const userNumData = bodyObj.user_num;
            
            if (type == null)
                return;

            // 캐시에서 닉네임을 바로 조회. 닉네임 캐시에서 사용자 번호를 기반으로 닉네임을 조회합니다.
            const userDetails = userCache[userNumData] || {};
		    const nickname = userDetails.nickname || 'Unknown';
		    const inputdate = userDetails.inputdate || 'Unknown date';
		
		    switch (type) {
		        case 'TALK':
					const newMessageElement = createChatMsgBox(userNumData, messageValue, nickname, inputdate);
					document.querySelector('.chatbox').appendChild(newMessageElement);
                    stompClient.send('/pub/chat/openChatMain/loadJoinedChatroomListRealTime/' + chatroomNum, {}, myUserNum);
                    scrollDown();
                    break;
            }
        } catch (error) {
            console.error("JSON 파싱 에러:", error);
            return;
        }
    } else {
        console.error("body가 존재하지 않습니다.");
        return;
    }
}