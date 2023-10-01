let stompClient;
let userNum;
let subscriptionForUpdateChatroom;	// 방 정보 업데이트 이벤트를 구독할 때 구독 정보를 저장하는 객체

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

function init()
{
	if (stompClient)
		stompClient.disconnect();
	
	stompClient = null;
	userNum = document.getElementById('userNum').value;
	subscriptionForUpdateChatroom = {};
	document.getElementById('searchInput').value = ''; // 검색창은 최초 실행시에 비워준다.
}

// 소켓을 만들어주는 역할을 하는 함수
function connect() {
    // 연결하고자하는 Socket 의 endPoint
    let socket = new SockJS('/ringo/ws-stomp');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
    
    if (stompClient === null) {	
        console.log("연결 실패. client를 찾을 수 없습니다.");
        return false;
    }
    
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
        stompClient.send('/pub/chat/openChatMain/loadChatRoomNumsByUserNum/' + userNum, {}, userNum);
        $openchat_btm.addClass('btn-primary').removeClass('btn-outline-primary');
        $dm_btn.addClass('btn-outline-primary').removeClass('btn-primary');
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
    
    //메시지 보낸사람 닉네임받아오기
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
			// 대신 기존에 가져온 내가 참여한 채팅방 전체 목록이 사라지기 때문에 다시 호출
			stompClient.send('/pub/chat/openChatMain/loadChatRoomNumsByUserNum/' + userNum, {}, userNum);
       		return;
		}
		
		stompClient.send('/pub/chat/openChatMain/searchByTitle/' + userNum, {}, title);
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
