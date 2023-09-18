
'use strict';

$(document).ready(function()
{
	init();
	connect();
});

let stompClient;
let username;
let chatroomInfo;
let chatroomNum;
let myChatroomLinkInfo;
let myUserNum;
let url;

// 초기화를 해주기 위한 코드, 실행 시점 차이로 인해 값을 받지 못하는 것을 방지.
function init()
{
	stompClient = null;
	username = null;
	
	chatroomInfo = document.getElementById('chatroom').value;
	chatroomNum = findValueByKey("chatroom_num", chatroomInfo)
	myChatroomLinkInfo = document.getElementById("myChatroomLink").value;
	myUserNum = findValueByKey("user_num", myChatroomLinkInfo);
	url = new URL(location.href).searchParams;
}

// 원하는 키 값을 getElementById('E_ID').value 형태로 받아오는 경우 value 안에 키를 찾아줌
// 찾을 키(키 이름, 가져온 value값) 
function findValueByKey(key, inputString) 
{
	// 정규 표현식 패턴: "key : {value}" 형태를 찾음
	const keyValueRegex = new RegExp(`${key}\\s*:\\s*{([^}]+)}`);
	const match = inputString.match(keyValueRegex);

	if (match) 
	{
		// 매치된 값을 가져옴
		const value = match[1].trim();
		return value;
	}
	else 
	{
		console.log(`${key} 값을 찾을 수 없습니다.`);
		return null;
	}
}

// 소켓을 만들어주는 역할을 하는 함수
function connect()
{
	// 연결하고자하는 Socket 의 endPoint
    let socket = new SockJS('/ringo/ws-stomp');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
    
    if (stompClient == null)
    {	
		console.log("연결 실패. client를 찾을 수 없습니다.");
    	return false;
    }
    
    // 메시지 보내는 버튼에 이벤트 연결
    document.getElementById("msg_submit").addEventListener("click", function() 
    {
		const chatCommon = 
		{
			type: 'TALK', // 메시지 타입
			chat_num: "", // 채팅 번호
			user_num: myUserNum, // 사용자 번호
			chatroom_num: chatroomNum, // 채팅방 번호
			message: document.getElementById("msg_input").value, // 메시지 내용
			inputdate: "", // 입력 날짜
			origin_file: "", // 원본 파일
			saved_file: "", // 저장된 파일
			photo_size: 0 // 사진 크기
		};
			
        stompClient.send('/pub/chat/openChatRoom/message/' + chatroomNum, {}, JSON.stringify(chatCommon));
        document.getElementById("msg_input").value = '';
    });
    
    return true;
}

// 접속할 시에 보낼 함수 
function onConnected() 
{
	// ChatCommon 객체를 생성
	const chatCommon = 
	{
		type: 'ENTER', // MessageType.ENTER와 동일
		chat_num: "", // 채팅 번호
		user_num: myUserNum, // 사용자 번호
		chatroom_num: chatroomNum, // 채팅방 번호
		message: "", // 메시지 내용
		inputdate: "", // 입력 날짜
		origin_file: "", // 원본 파일
		saved_file: "", // 저장된 파일
		photo_size: 0 // 사진 크기
	};

	stompClient.send('/pub/chat/openChatRoomEnter/' + chatroomNum, {}, JSON.stringify(chatCommon));
	
	// 입장, 퇴장 관련 메시지를 받는 이벤트
	stompClient.subscribe('/sub/chat/openChatRoom/message/state/' + chatroomNum, onMessageForState)
	// 메시지를 받는 이벤트 => /sub/chat/openChatRoom/message/채팅방번호 로 구독한다
	stompClient.subscribe('/sub/chat/openChatRoom/message/' + chatroomNum, onMessageReceived);
}

// 접속 실패 후, 에러 발생시 실행하는 함수
function onError(error) 
{
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// 입장, 퇴장 시에 실행하는 함수
function onMessageForState(message)
{
	console.log(message);
}

// 채팅 메시지를 받을때 실행하는 함수
function onMessageReceived(message) 
{
    //console.log(message);
    
    // message의 body 속성을 가져와서 파싱
    const bodyString = message.body;
    if (bodyString) 
    {
        try 
        {
            const bodyObj = JSON.parse(bodyString);
            const messageValue = bodyObj.message;  // "message" 키의 값을 가져옴
            const type = bodyObj.type;  // "message" 키의 값을 가져옴
            //console.log("메시지 :", messageValue);
            //console.log("타입 :", type);  //
            
            if (type == null)
            	return;
            	
            switch (type)
            {
				// case가 TALK인 경우에는 새 메시지를 추가해서 붙혀준다.
				case 'TALK':
					console.log("메시지:", messageValue);
					break;
			}
        } 
        catch (error) 
        {
            console.error("JSON 파싱 에러:", error);
        }
    } 
    else 
    {
        console.error("body가 존재하지 않습니다.");
    }
}

/*
	참고 - 메시지 보내는 예시
	
	// ChatCommon 객체를 생성
	const chatCommon = 
	{
		type: 'ENTER', // MessageType.ENTER와 동일
		chat_num: "", // 채팅 번호
		user_num: myUserNum, // 사용자 번호
		chatroom_num: chatroomNum, // 채팅방 번호
		message: "", // 메시지 내용
		inputdate: "", // 입력 날짜
		origin_file: "", // 원본 파일
		saved_file: "", // 저장된 파일
		photo_size: 0 // 사진 크기
	};

	// stompClient.send()를 사용하여 메시지 전송
	stompClient.send('/pub/chat/openChatRoomEnter/' + chatroomNum, {}, JSON.stringify(chatCommon));
*/