$(document).ready(function()
{
	init();
	connect();
});

let stompClient;
let userNum;
let subscriptionForUpdateChatroom;	// 방 정보 업데이트 이벤트를 구독할 때 구독 정보를 저장하는 객체 

window.addEventListener('beforeunload', function(event) 
{
    if (stompClient !== null) 
    {
        stompClient.disconnect();
    }
    
    // 원하는 경우, 사용자에게 경고 메시지를 표시할 수도 있습니다.
    //event.returnValue = '';
});

function init()
{
	if (stompClient)
		stompClient.disconnect();
	
	stompClient = null;
	userNum = document.getElementById('userNum').value;
	subscriptionForUpdateChatroom = {};
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
    
    // 검색 관련 이벤트 추가
    document.getElementById('searchInput').addEventListener('keyup', function()
    {
		searchByTitle(document.getElementById('searchInput').value);
	});
    
    return true;
}

function onConnected() 
{
	// 채팅방 정보를 가져오기 위한 호출
	//stompClient.send('/pub/chat/openChatMain/loadJoinedChatroomListRealTime/' + chatroom_num, {}, userNum);
	// 채팅방 정보를 받기 위한 이벤트 연결
	//stompClient.subscribe('/sub/chat/openChatMain/loadJoinedChatroomListRealTime/' + chatroom_num, loadJoinedChatroomListRealTime);
	
	// 자신이 잠가한 채팅방의 번호들을 자신의 고유번호로 가져오는 것을 요청
	stompClient.send('/pub/chat/openChatMain/loadChatRoomNumsByUserNum/' + userNum, {}, userNum);
	// 자신이 잠가한 채팅방의 번호들을 자신의 고유번호로 가져오는 것을 받기 위한 이벤트 연결
	stompClient.subscribe('/sub/chat/openChatMain/loadChatRoomNumsByUserNum/' + userNum, loadChatRoomNumsByUserNum);
	
	// 검색창에 방 제목을 입력할 시 결과를 받기 위해 이벤트 연결
	stompClient.subscribe('/sub/chat/openChatMain/searchByTitle/' + userNum, searchResultByTitle);
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
	// 기존에 있는 채팅방 리스트를 삭제
	clearChatlist();
	
	let jsonData = JSON.parse(data.body);
	
	jsonData.forEach(item => {
		createChatroomThumbnail(item.chatroom_num, item.title, item.inputdate, item.message);
	});
}

function createChatroomThumbnail(chatroom_num, title, inputdate, message)
{
	 // chatlist의 div 요소 접근
    let chatlist = document.querySelector('.chatlist');
    
    // 새로운 block div 요소를 생성
    let blockDiv = document.createElement('div');
    blockDiv.className = 'block';
    
    // details div 요소를 생성
    let detailsDiv = document.createElement('div');
    detailsDiv.className = 'details';
    blockDiv.appendChild(detailsDiv);
    
    // listHead div 요소를 생성, details에 append
    let listHeadDiv = document.createElement('div');
    listHeadDiv.className = 'listHead';
    detailsDiv.appendChild(listHeadDiv);
    
    // hidden input 요소를 생성, chatroom_num 값을 설정
    let hiddenInput = document.createElement('input');
    hiddenInput.type = 'hidden';
    hiddenInput.value = chatroom_num;
    listHeadDiv.appendChild(hiddenInput);
    
    // h5 요소를 생성, title 값을 설정
    let h5 = document.createElement('h5');
    h5.textContent = title;
    listHeadDiv.appendChild(h5);
    
    // p 요소를 생성, time 클래스를 추가하며 inputdate 값을 설정
    let timeP = document.createElement('p');
    timeP.className = 'time';
    timeP.textContent = inputdate;
    listHeadDiv.appendChild(timeP);
    
    // message_p div 요소를 생성, details에 append
    let messageDiv = document.createElement('div');
    messageDiv.className = 'message_p';
    detailsDiv.appendChild(messageDiv);
    
    // p 요소를 생성, message 값을 설정
    let messageP = document.createElement('p');
    messageP.textContent = message;
    messageDiv.appendChild(messageP);
    
    // 생성한 blockDiv 요소를 chatlist에 append
    chatlist.appendChild(blockDiv);
    
    // blockDiv 클릭 이벤트 리스너 추가
    blockDiv.addEventListener('click', function() 
    {
        moveToChatroom(chatroom_num);
    });
}

function moveToChatroom(chatroom_num) 
{
    // chatroom 페이지로 이동하면서 chatroom_num을 파라미터로 전달
    window.location.href = "/ringo/chat/openChatRoomEnter?chatroom_num=" + chatroom_num;
}

function clearChatlist()
{
    let chatlist = document.querySelector('.chatlist');
    
    // 모든 자식 요소를 삭제
    while (chatlist.firstChild) {
        chatlist.removeChild(chatlist.firstChild);
    }
}

function loadChatRoomNumsByUserNum(data)
{
	let jsonData = JSON.parse(data.body);
	// 한번만 불러와도 되기 때문에 사용한 함수
	let isLoaded = false;
	
	// 불러온 채팅방 데이터를 기반으로 
	jsonData.forEach(item => 
	{
		subscribe(item);
		
		if (!isLoaded)
		{
			stompClient.send('/pub/chat/openChatMain/loadJoinedChatroomListRealTime/' + item, {}, userNum);
			isLoaded = true;
		}
	});
}

// 새로운 이벤트를 구독하기 위한 기능
function subscribe(endpoint)
{
	// 이미 구독 중이라면 구독을 해지
	if (subscriptionForUpdateChatroom[endpoint])
		subscriptionForUpdateChatroom[endpoint].unsubscribe();
	
	// 채팅방 정보를 받기 위한 이벤트 연결
	subscriptionForUpdateChatroom[endpoint] = stompClient.subscribe('/sub/chat/openChatMain/loadJoinedChatroomListRealTime/' + endpoint, loadJoinedChatroomListRealTime);
}

function searchByTitle(title)
{
	if (stompClient && stompClient.connected)
	{
		if (title.trim() === '')
		{
			// 검색어가 비어 있으면 요청을 보내지 않는다.
       		return;
		}
		
		stompClient.send('/pub/chat/openChatMain/searchByTitle/' + userNum, {}, title);
	}
}

function searchResultByTitle(data)
{
	if (data == null)
		return;
		
	console.log(data.body);
}