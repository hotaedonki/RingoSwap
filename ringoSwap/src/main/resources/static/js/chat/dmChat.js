let stompClient;
let myAcc;								// 내 계정과 관련한 정보들
let otherAcc;							// 다른 사람 계정과 관련된 정보들
let dmChatroom;							// DM 채팅방 관련 정보
let DMRoomNum;							// DM 방번호
let myNickname;							// 내 닉네임
let otherNickname;						// 다른 사람 닉네임
let myUserNum;							// 내 유저 번호
let subscriptionForDMChatroom;			// DM 채팅방 구독 정보를 담기 위한 변수
let subscriptionForUpdateChatroom;		// 오픈 채팅방 구독 정보를 담기 위한 변수
let url;								// 현재 이 페이지의 URL를 담는 변수

$(document).ready(function()
{
	init();
	connect();
});

window.addEventListener('beforeunload', function(event) 
{
    if (stompClient !== null) 
        stompClient.disconnect();
    
    // 원하는 경우, 사용자에게 경고 메시지를 표시할 수도 있습니다.
    //event.returnValue = '';
});

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


function init()
{
	if (stompClient)
		stompClient.disconnect();
		
	stompClient = null;
	
	myAcc = document.getElementById('myAcc').value;
	otherAcc = document.getElementById('otherAcc').value;
	dmChatroom = document.getElementById('dmChatroom').value;
	DMRoomNum = findValueByKey('dm_chatroom_num', dmChatroom);
	myNickname = findValueByKey('nickname', myAcc);
	myUserNum = findValueByKey('user_num', myAcc);
	otherNickname = findValueByKey('nickname', otherAcc);
	subscriptionForDMChatroom = {};
	url = new URL(location.href).searchParams;
	document.getElementById('searchInput').value = '';	// 검색창은 최초 실행시에 비워준다.
	
	//console.log(" [myUserNum] - " + myUserNum);
	//console.log(" [otherNickname] - " + otherNickname);
}

function connect()
{
	let socket = new SockJS('/ringo/ws-stomp');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, onConnected);
	
	if (stompClient === null) 
	{
		console.log("연결 실패. client를 찾을 수 없습니다.");
		return false;
	}
	
	
	// 검색 관련 이벤트 추가
    $('#searchInput').on('keyup', function() {
        searchByTitle($(this).val());
    });
    
	
}

function onConnected()
{
	// 입장, 퇴장 관련 메시지를 받는 이벤트
	stompClient.subscribe('/sub/chat/DMChat/message/state/' + DMRoomNum, onMessageForState);
	// 메시지를 받는 이벤트 => /sub/chat/DMChatRoom/message/채팅방번호 로 구독한다
	stompClient.subscribe('/sub/chat/DMChat/message/' + DMRoomNum, onMessageReceived);
	
	// 자신이 참가한 채팅방의 번호들을 자신의 고유번호로 가져오는 것을 요청
	stompClient.send('/pub/chat/DMChat/loadDMChatRoomNumsByUserNum/' + myUserNum, {}, myUserNum);
	
	// 자신이 참가한 채팅방의 번호들을 자신의 고유번호로 가져오는 것을 받기 위한 이벤트 연결
	stompClient.subscribe('/sub/chat/DMChat/loadDMChatRoomNumsByUserNum/' + myUserNum, loadDMChatRoomNumsByUserNum);
	
	// 검색창에 방 제목을 입력할 시 결과를 받기 위해 이벤트 연결
	stompClient.subscribe('/sub/chat/DMChat/searchByTitle/' + myUserNum, searchResultByTitleDMChat);
}

// 입장, 퇴장 시에 실행하는 함수
function onMessageForState(message)
{
	console.log(message);
}

// 메시지를 받는 이벤트
function onMessageReceived(message) 
{
	
}

// 
function loadDMChatRoomNumsByUserNum(data)
{
	// 검색중에는 새로운 이벤트를 막는다.
	if (document.getElementById('searchInput').value.length > 0)
	{
		return;
	}
	
	let jsonData = JSON.parse(data.body);
	
	console.log("jsonData : " + jsonData);
	
	console.log("DMRoomNum : " + DMRoomNum);
	// 불러온 채팅방 데이터를 기반으로 
	jsonData.forEach(item => 
	{
		console.log("item : " + item);
		subscribeForDMChat(item);
	});
	
	stompClient.send('/pub/chat/DMChat/loadJoinedDMChatroomListRealTime/' + DMRoomNum, {}, myUserNum);
	
}

// 새로운 이벤트를 구독하기 위한 기능
function subscribeForDMChat(endpoint)
{
	// 이미 구독 중이라면 구독을 해지
	if (subscriptionForDMChatroom[endpoint])
		subscriptionForDMChatroom[endpoint].unsubscribe();
	
	// 채팅방 정보를 받기 위한 이벤트 연결
	subscriptionForDMChatroom[endpoint] = stompClient.subscribe('/sub/chat/DMChat/loadJoinedDMChatroomListRealTime/' + endpoint, loadJoinedDMChatroomListRealTime);
}

function loadJoinedDMChatroomListRealTime(data)
{
	
	console.log("loadJoinedDMChatroomListRealTime");
	// 검색중에는 새로운 이벤트가 들어와 내가 참여한 채팅방 리스트를 새로고침하는 것을 막는다.
	if (document.getElementById('searchInput').value.length > 0)
	{
		return;
	}
	
	if (data == null)
		return;
	
	let jsonData = JSON.parse(data.body);
	
	console.log(jsonData);
	
	
	// 기존에 있는 채팅방 리스트를 삭제
	clearChatlist();
	
	
	jsonData.forEach(item => {
		createChatroomThumbnail(item.chatroom_num, item.title, item.inputdate, item.message);
	});
	
	
	
}

function searchResultByTitleDMChat(data)
{
	
}

function clearChatlist() 
{
    let chatlist = document.querySelector('.chatlist');
    
    // 모든 자식 요소를 삭제
    while (chatlist.firstChild) {
        chatlist.removeChild(chatlist.firstChild);
    }
}