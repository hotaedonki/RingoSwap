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
	stompClient.send('/pub/chat/openChatMain/loadJoinedChatroomListRealTime/' + userNum, {}, userNum);
	
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

function loadJoinedChatroomListRealTime(data)
{
	let jsonData = JSON.parse(data.body);
	
	jsonData.forEach(item => {
		createChatroomThumbnail(item.chatroom_num, item.title, item.inputdate, item.message);
	});
}

function createChatroomThumbnail(chatroom_num, title, inputdate, message)
{
	 // chatlist의 div 요소를 찾습니다.
    let chatlist = document.querySelector('.chatlist');
    
    // 새로운 block div 요소를 생성합니다.
    let blockDiv = document.createElement('div');
    blockDiv.className = 'block';
    
    // details div 요소를 생성합니다.
    let detailsDiv = document.createElement('div');
    detailsDiv.className = 'details';
    blockDiv.appendChild(detailsDiv);
    
    // listHead div 요소를 생성하고, details에 append 합니다.
    let listHeadDiv = document.createElement('div');
    listHeadDiv.className = 'listHead';
    detailsDiv.appendChild(listHeadDiv);
    
    // hidden input 요소를 생성하고, chatroom_num 값을 설정합니다.
    let hiddenInput = document.createElement('input');
    hiddenInput.type = 'hidden';
    hiddenInput.value = chatroom_num;
    listHeadDiv.appendChild(hiddenInput);
    
    // h5 요소를 생성하고, title 값을 설정합니다.
    let h5 = document.createElement('h5');
    h5.textContent = title;
    listHeadDiv.appendChild(h5);
    
    // p 요소를 생성하고, time 클래스를 추가하며 inputdate 값을 설정합니다.
    let timeP = document.createElement('p');
    timeP.className = 'time';
    timeP.textContent = inputdate;
    listHeadDiv.appendChild(timeP);
    
    // message_p div 요소를 생성하고, details에 append 합니다.
    let messageDiv = document.createElement('div');
    messageDiv.className = 'message_p';
    detailsDiv.appendChild(messageDiv);
    
    // p 요소를 생성하고, message 값을 설정합니다.
    let messageP = document.createElement('p');
    messageP.textContent = message;
    messageDiv.appendChild(messageP);
    
    // 생성한 blockDiv 요소를 chatlist에 append 합니다.
    chatlist.appendChild(blockDiv);
}