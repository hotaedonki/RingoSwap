package net.ringo.ringoSwap.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.service.ChatService;

/*
웹 소켓 클라이언트로부터 채팅 메시지를 전달받아 채팅 메시지 객체로 변환
전달받은 메시지에 담긴 채팅방 Id로 발송 대상 채팅방 정보를 조회
해당 채팅방에 입장해 있는 모든 클라이언트(Websocket Session)에게 타입에 따른 메시지 발송
*/

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler
{
	private final ObjectMapper mapper;
	private final ChatService service;
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception 
	{
		String payload = (String) message.getPayload();
        log.info("payload : {}", payload);
	    //TextMessage intialGretting = new TextMessage("Welcome to Chat Server");
	    //JSON -> Java Object
        
        ChatCommon chatMessage = mapper.readValue(payload, ChatCommon.class);
        log.info("handle chatMessage / {}", chatMessage.toString());
        
        Chatroom room = service.getChatroomById(chatMessage.getChatroom_num());
        log.info("handle room / {}", room.toString());
        
        room.handleAction(session, chatMessage, service);
	}
	
    /** Client가 접속 시 호출되는 메서드*/
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception 
    {
        log.info(session + " 클라이언트 접속");
    }
    
    /** client가 접속 시 호출되는 메서드*/
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception 
    {
        log.info(session + " 클라이언트 접속 해제");
    }
}