
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

function connect()
{
	// 연결하고자하는 Socket 의 endPoint
    let socket = new SockJS('/ringo/ws-stomp');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
    
    //alert("연결 완료");
}

function onConnected() 
{
	// ChatCommon 객체를 생성
	const chatCommon = {
		type: 'ENTER', // MessageType.ENTER와 동일
		chat_num: 123, // 채팅 번호 (원하는 값으로 변경)
		user_num: 456, // 사용자 번호 (원하는 값으로 변경)
		chatroom_num: 789, // 채팅방 번호 (원하는 값으로 변경)
		message: "입장합니다", // 메시지 내용 (원하는 내용으로 변경)
		inputdate: "2023-09-12", // 입력 날짜 (원하는 날짜로 변경)
		origin_file: "", // 원본 파일 (원하는 값으로 변경)
		saved_file: "", // 저장된 파일 (원하는 값으로 변경)
		photo_size: 0 // 사진 크기 (원하는 값으로 변경)
	};

	// stompClient.send()를 사용하여 메시지 전송
	stompClient.send('/pub/chat/openChatRoomEnter/', {}, JSON.stringify(chatCommon));

	
	// sub 할 url => /sub/chat/room/roomId 로 구독한다
	stompClient.subscribe('/sub/chat/openChatRoom/' + chatroomNum, onMessageReceived);
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// 메시지 전송때는 JSON 형식을 메시지를 전달한다.
function sendMessage(event) {
    let messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        let chatMessage = {
            "roomId": roomId,
            sender: username,
            message: messageInput.value,
            type: 'TALK'
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}