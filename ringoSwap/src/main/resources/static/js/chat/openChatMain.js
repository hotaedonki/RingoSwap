'use strict';

$(document).ready(function()
{
	init();
	connect();
});

let stompClient;
let userNum;

function init()
{
	stompClient = null;
	userNum = document.getElementById('userNum').value;
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
    
    return true;
}

function onConnected() 
{
	stompClient.send('/pub/chat/openChatMain/loadJoinedChatroomListRealTime/', {}, userNum);
	
	// 입장, 퇴장 관련 메시지를 받는 이벤트
	stompClient.subscribe('/sub/chat/openChatMain/loadJoinedChatroomListRealTime/' + userNum, loadJoinedChatroomListRealTime);
}

function onError() 
{
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}

function createChatRoom()
{
	let title = document.getElementById('title').value;
	let lang_category = document.getElementById('lang_category').value;
	let capacity = document.getElementById('capacity').value;
	
	if (title.length <= 1)
	{
		alert("방제는 최소 두 글자 이상이어야합니다.");
		return;
	}
	
	if (lang_category.length <= 0)
	{
		alert("채팅방 언어를 선택해주세요.");
		return;
	}

	$.ajax({
		url: 'createOpenChatroom',
		type: 'post',
		data: { title: title, lang_category: lang_category, capacity: capacity },
		success: function(isSuccess) 
		{
			if (!isSuccess)
			{
				alert("새 채팅방 생성 실패");
				return;
			}
			
		},
		error: function(e) 
		{
			alert(JSON.stringify(e));
		}
	});
}

function loadJoinedChatroomListRealTime(jsonData)
{
	console.log(jsonData);
}

function loadChatroomList()
{
	
}