let stompClient;
let username;
let chatroomInfo;
let chatroomNum;
let myChatroomLinkInfo;
let myUserNum;
let url;
let subscriptionForUpdateChatroom;
let emojioneAreaInstance;
let userCache = {}; 
let myTransLang;

$(document).ready(function()
{
	init();
	connect();
	$(document).on('click', '#return-to-chat-main', returnToChatMain);
    $(document).on('click', '#out-chat-room', outChatRoom);
    $(document).on('click', '.translate', chatTranslate);
    
	const emojiArea = $("#msg_input").emojioneArea({
		pickerPosition: "top",
	});
	
	emojioneAreaInstance = emojiArea[0].emojioneArea;

	emojioneAreaInstance.on("keyup", function(editor, event) {
        if (event.which === 13) {
            event.preventDefault();
            let messageText = emojioneAreaInstance.getText();

            if (messageText.trim() !== '') {
                $("#msg_submit").click();
                emojioneAreaInstance.setText('');
            }
        }
    });
    
    console.log(userCache);
    setNicknamesForExistingMessages(userCache);
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
    stompClient.connect({}, onConnected);
    
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
			message: emojioneAreaInstance.getText().trim(), // 메시지 내용
			inputdate: "", // 입력 날짜
			origin_file: "", // 원본 파일
			saved_file: "", // 저장된 파일
			photo_size: 0 // 사진 크기
		};
			
        stompClient.send('/pub/chat/openChatRoom/message/' + chatroomNum, {}, JSON.stringify(chatCommon));
        emojioneAreaInstance.setText('');;
    });
    
     // 검색 관련 이벤트 추가
    $('#searchInput').on('keyup', function() {
        searchByTitle($(this).val());
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

	// 채팅방에 입장할 때 사용자의 닉네임 로드
	loadAllBasicDetailsForChatroom(chatroomNum);
    loadAllNicknamesForChatroom(chatroomNum);
}

function setNicknamesForExistingMessages(userCache) {
    const chatMessages = document.querySelectorAll('#msg_chatBoxArea li');

    chatMessages.forEach(chatMessage => {
        const userNum = chatMessage.getAttribute('data-user-num');
        const messageContent = chatMessage.querySelector('p').textContent;
		
        // userCache에서 사용자 정보를 조회
        const userDetails = userCache[userNum] || {};
        const nickname = userDetails.nickname || 'Unknown';
        const inputdate = userDetails.inputdate || 'Unknown date';

        // createChatMsgBox 함수를 활용하여 새로운 메시지 박스를 생성
        const newChatMessageBox = createChatMsgBox(userNum, messageContent, nickname, inputdate);
        
        // 기존의 메시지 박스를 새로 생성한 메시지 박스로 대체
        chatMessage.parentNode.replaceChild(newChatMessageBox, chatMessage);
    });
    scrollDown();
}

function loadAllBasicDetailsForChatroom(chatroomNum) {
    $.ajax({
        url: 'allUserBasicDetails',
        type: 'post',
        data: { chatroom_num: chatroomNum },
        success: function(data) {
            data.forEach(user => {
                userCache[user.USER_NUM] = {
                    nickname: user.NICKNAME,
                    user_id: user.USER_ID,
                    trans_lang: user.TRANS_LANG
                };
                if(myUserNum == user.USER_NUM){
					myTransLang = user.TRANS_LANG;
				}
                addParticipantToParticipantsList(user);
            });
        },
        error: function(error) {
            console.error("Error fetching basic user details:", error);
        }
    });
}


function loadAllNicknamesForChatroom(chatroomNum) {
	console.log(chatroomNum);
    $.ajax({
        url: 'allUserNicknameSendMessage',
        type: 'post',
        data: { chatroom_num: chatroomNum },
        success: function(data) {
            data.forEach(user => {
                userCache[user.USER_NUM] = {
                    nickname: user.NICKNAME,
                    user_id: user.USER_ID,
			        inputdate: user.FORMATTED_INPUTDATE,
			        trans_lang: user.TRANS_LANG
                };
                console.log(userCache);
                if(myUserNum == user.USER_NUM){
					myTransLang = user.TRANS_LANG;
				}
				console.log(myTransLang);
                addParticipantToParticipantsList(user);
            });
        setNicknamesForExistingMessages(userCache);
        },
        error: function(error) {
            console.error("Error fetching nicknames:", error);
        }
    });
}

function addParticipantToParticipantsList(user) {
    const userNum = user.USER_NUM;
    if (!$(`.participant-item[data-user-num='${userNum}']`).length) {
        const profileImageUrl = user.USER_ID ? '../member/memberProfilePrint?user_id=' + user.USER_ID : ''; // 기본 이미지 URL이 필요하다면 수정해야 합니다.
        const nickname = user.NICKNAME || 'Unknown';

        // 사용자의 닉네임과 프로필 사진을 포함하는 HTML 요소 생성
        let $participantDiv = $('<div></div>').addClass('participant-item mb-3 showOffcanvasWithUserData').attr('data-user-num', userNum).attr('data-user-name', nickname);
        let $profilePicElement = $('<img>').attr('src', profileImageUrl).addClass('participant-profile-pic');
        let $nicknameElement = $('<span></span>').addClass('participant-nickname').text(nickname);

        $participantDiv.append($profilePicElement).append($nicknameElement);

        // 생성된 HTML 요소를 participants_list에 추가
        $('.participants_list .participants').append($participantDiv);
    }
}


// 입장, 퇴장 시에 실행하는 함수
function onMessageForState(message)
{
	console.log(message);
}

function onMessageReceived(message) {
    // message의 body 속성을 가져와서 파싱
    const bodyString = message.body;
    console.log(bodyString);
    if (bodyString) {
        try {
            const bodyObj = JSON.parse(bodyString);
            const messageValue = bodyObj.message;
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


function scrollDown()
{
	let chatBox = document.querySelector('.chatbox'); // 채팅 박스에 대한 참조
	console.log(chatBox);
    chatBox.scrollTop = chatBox.scrollHeight; // 스크롤을 맨 아래로 이동
}

function createChatMsgBox(userNum, message, nickname = '', inputdate = '') {
    let profilePic;
    const userDetails = userCache[userNum] || {};
    const userId = userDetails.user_id;
    let profileImageUrl = profilePic; // 기본값 설정
    if(userId) {
        profileImageUrl = '../member/memberProfilePrint?user_id=' + userId;
    }

    let $liElement = $('<li></li>').attr('data-user-num', userNum).addClass('chat-message');
    let $contentDiv = $('<div></div>').addClass('msg_content row');
    let $nicknameDateElement = $('<div></div>').addClass('user-info col-12 row showOffcanvasWithUserData').attr('data-user-name', nickname)
                                               .append($('<span></span>').addClass('nickname').text(nickname))
                                               .append($('<span></span>').addClass('message-date').text(inputdate));
    let $profilePicElement = $('<img>').attr('src', profileImageUrl).addClass('profile-pic col-3 me-0 ms-2 pe-0 ps-0 showOffcanvasWithUserData').attr('data-user-name', nickname);
    let $pElementRow = $('<div></div>').addClass('col-9 row');
    let $pElement = $('<p></p>').addClass('col-8 message-area').text(message);
	let translateBtn = $(`<i class="col-4 bi bi-translate translate"></i>`);
    if (userNum == myUserNum) {
        $liElement.addClass('chat outcoming').append($pElement);
    } else {
        $pElementRow.append($nicknameDateElement)
                    .append($pElement)
                    .append(translateBtn);
        $contentDiv.append($profilePicElement)
                   .append($pElementRow);
        $liElement.addClass('chat incoming').append($contentDiv);
    }

    return $liElement[0];
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

function returnToChatMain() {
	window.location.href = '../chat/openChatMain';
}

function outChatRoom() {
    const dataToSend = {
        chatroom_num: chatroomNum,
        user_num: myUserNum
    };
	
	console.log(dataToSend);
    $.ajax({
        url: 'outChatRoom',
        type: 'POST',
        data: JSON.stringify(dataToSend), 
        contentType: 'application/json',  
        dataType: 'json', 
        success: function(response) {
            if (response.success) {
				confirm("Do you want to delete the room?")
                checkAndDeleteEmptyChatroom(chatroomNum);
                window.location.href = '../chat/openChatMain?refresh=1';
            } else {
                alert('Error leaving the chatroom: ' + response.message);
            }
        },
        error: function(error) {
            console.error("Error leaving chatroom:", error);
        }
    });
}

function checkAndDeleteEmptyChatroom(chatroomNum) {
	$.ajax({
		url:'deleteRoom'
		, type: 'post'
		, data: { chatroomNum: chatroomNum }
		, success: function(response) {
			
		},
		error: function(error) {
			 console.error("Error delete chatroom:", error);
		}
	})
}

function chatTranslate() {
	let text = $(this).closest(".chat-message").find(".message-area");
	let part = text.text();
	
	console.log(part);
	let targetLang = myTransLang;
    $.ajax({
        url: '/ringo/translate/feed',
        type: 'post',
        data: { text: part, targetLang: targetLang },
        dataType: 'text',
        success: function(translateText) {
			console.log(translateText);
            text.text(translateText);
        },
        error: function(e) {
            console.log(e);
        }
    });
}

