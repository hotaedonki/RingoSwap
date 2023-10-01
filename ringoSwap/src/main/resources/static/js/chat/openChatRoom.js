let stompClient;
let username;
let chatroomInfo;
let chatroomNum;
let myChatroomLinkInfo;
let myUserNum;
let url;
let subscriptionForUpdateChatroom;

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

// 초기화를 해주기 위한 코드, 실행 시점 차이로 인해 값을 받지 못하는 것을 방지.
function init()
{
	if (stompClient)
		stompClient.disconnect();
	
	stompClient = null;
	username = null;
	
	chatroomInfo = document.getElementById('chatroom').value;
	chatroomNum = findValueByKey("chatroom_num", chatroomInfo)
	myChatroomLinkInfo = document.getElementById("myChatroomLink").value;
	myUserNum = findValueByKey("user_num", myChatroomLinkInfo);
	url = new URL(location.href).searchParams;
	subscriptionForUpdateChatroom = {};
	document.getElementById('searchInput').value = ''; // 검색창은 최초 실행시에 비워준다.
	
	console.log("init chatroomNum - " + chatroomNum);
	console.log("init usernum - " + myUserNum);
	
	scrollDown();
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
    
    if (stompClient === null)
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
    
     // 검색 관련 이벤트 추가
    $('#searchInput').on('keyup', function() {
        searchByTitle($(this).val());
    });
    
    // DM, OpenChat 버튼 이벤트 연결
	let $dm_btn = $('#DM_btn');
    let $openchat_btm = $('#OpenChat_btn');
	
	$dm_btn.on('click', function() {
        $dm_btn.addClass('btn-primary').removeClass('btn-outline-primary');
        $openchat_btm.addClass('btn-outline-primary').removeClass('btn-primary');
    });
    
    $openchat_btm.on('click', function() {
        stompClient.send('/pub/chat/openChatMain/loadChatRoomNumsByUserNum/' + myUserNum, {}, myUserNum);
        $openchat_btm.addClass('btn-primary').removeClass('btn-outline-primary');
        $dm_btn.addClass('btn-outline-primary').removeClass('btn-primary');
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
	
	// 채팅방 정보를 가져오기 위한 호출
	//stompClient.send('/pub/chat/openChatMain/loadJoinedChatroomListRealTime/' + myUserNum, {}, myUserNum);
	// 채팅방 정보를 받기 위한 이벤트 연결
	//stompClient.subscribe('/sub/chat/openChatMain/loadJoinedChatroomListRealTime/' + myUserNum, loadJoinedChatroomListRealTime);
	
	// 자신이 참가한 채팅방의 번호들을 자신의 고유번호로 가져오는 것을 요청
	stompClient.send('/pub/chat/openChatMain/loadChatRoomNumsByUserNum/' + myUserNum, {}, myUserNum);
	// 자신이 참가한 채팅방의 번호들을 자신의 고유번호로 가져오는 것을 받기 위한 이벤트 연결
	stompClient.subscribe('/sub/chat/openChatMain/loadChatRoomNumsByUserNum/' + myUserNum, loadChatRoomNumsByUserNum);
	
	// 검색창에 방 제목을 입력할 시 결과를 받기 위해 이벤트 연결
	stompClient.subscribe('/sub/chat/openChatMain/searchByTitle/' + myUserNum, searchResultByTitle);
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
            const messageValue = bodyObj.message;
            const type = bodyObj.type;
            const userNumData = bodyObj.user_num;
            
            if (type == null)
            	return;
            	
            switch (type)
            {
				// case가 TALK인 경우에는 새 메시지를 추가해서 붙혀준다.
				case 'TALK':
					console.log("메시지:", messageValue);
					createChatMsgBox(userNumData, messageValue);
					
					// 채팅방 정보를 가져오기 위한 호출
					stompClient.send('/pub/chat/openChatMain/loadJoinedChatroomListRealTime/' + chatroomNum, {}, myUserNum);
					scrollDown();
					break;
			}
			
			return;
        } 
        catch (error) 
        {
            console.error("JSON 파싱 에러:", error);
            return;
        }
    } 
    else 
    {
        console.error("body가 존재하지 않습니다.");
        return;
    }
}

function scrollDown()
{
	let chatBox = document.querySelector('.chatbox'); // 채팅 박스에 대한 참조
    chatBox.scrollTop = chatBox.scrollHeight; // 스크롤을 맨 아래로 이동
}

// user_num이 자신의 아이디면 
function createChatMsgBox(userNum, message)
{
	const liElement = document.createElement('li');
	const pElement = document.createElement('p');

	if (userNum == myUserNum)
	{
		liElement.classList.add('chat', 'outcoming');
	}
	else
	{
		liElement.classList.add('chat', 'incoming');
	}
	pElement.setAttribute('text', message);
	pElement.textContent = message;  // 실제 텍스트 내용도 '안녕'으로 설정

	// 3. div의 자식으로 p를 추가
	liElement.appendChild(pElement);

	// 4. 부모 태그에 div 추가 (예를 들어, body 태그가 부모일 경우)
	document.getElementById('msg_chatBoxArea').appendChild(liElement);
}

function loadJoinedChatroomListRealTime(data)
{
	// 검색중에는 새로운 이벤트가 들어와 내가 참여한 채팅방 리스트를 새로고침하는 것을 막는다.
	if (document.getElementById('searchInput').value.length > 0)
	{
		return;
	}
	
	if (data == null)
		return;
	
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
    let $chatlist = $('.chatlist');

    // 새로운 block div 요소를 생성
    let $blockDiv = $('<div>').addClass('block');
    // details, listHead div 요소를 생성 및 내용 추가
    let $detailsDiv = $('<div>').addClass('details').append(
        $('<div>').addClass('listHead').append(
            $('<input>').attr('type', 'hidden').val(chatroom_num),
            $('<h5>').text(title),
            $('<p>').addClass('time').text(inputdate)
        ),
        $('<div>').addClass('message_p').append(
            $('<p>').text(message)
        )
    );
    // blockDiv에 detailsDiv 추가 및 chatlist에 blockDiv 추가
    $blockDiv.append($detailsDiv).appendTo($chatlist);
	$blockDiv.append('<hr>');
    // blockDiv 클릭 이벤트 리스너 추가
    $blockDiv.on('click', function() {
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
	// 검색중에는 새로운 이벤트를 막는다.
	if (document.getElementById('searchInput').value.length > 0)
	{
		return;
	}
	
	let jsonData = JSON.parse(data.body);
	
	// 불러온 채팅방 데이터를 기반으로 
	jsonData.forEach(item => 
	{
		subscribe(item);
	});
	
	stompClient.send('/pub/chat/openChatMain/loadJoinedChatroomListRealTime/' + chatroomNum, {}, myUserNum);
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
			// 대신 기존에 가져온 내가 참여한 채팅방 전체 목록이 사라지기 때문에 다시 호출
			stompClient.send('/pub/chat/openChatMain/loadChatRoomNumsByUserNum/' + myUserNum, {}, myUserNum);
       		return;
		}
		
		stompClient.send('/pub/chat/openChatMain/searchByTitle/' + myUserNum, {}, title);
	}
}

function searchResultByTitle(data)
{
	if (data == null)
		return;
	
	// 기존에 있는 채팅방 리스트를 삭제
	clearChatlist();
	
	let jsonData = JSON.parse(data.body);
	
	jsonData.forEach(item => {
		createChatroomThumbnail(item.chatroom_num, item.title, item.inputdate, item.message);
	});
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
	
	! 전달할 값이 없는 경우에는
	stompClient.send('/pub/chat/openChatRoomEnter/' + chatroomNum, {}, "");
*/