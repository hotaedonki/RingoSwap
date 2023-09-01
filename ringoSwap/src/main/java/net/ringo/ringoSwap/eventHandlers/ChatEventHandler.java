package net.ringo.ringoSwap.eventHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;

// DB쪽이 아닌, '실시간' 채팅 이벤트 처리를 위한 핸들러 클래스
@Component
public class ChatEventHandler 
{
	// 핸들러가 이벤트를 추가하기 위해 SocketIOServer를 가져와야 한다.
	private final SocketIOServer socketIOServer;
	
    @Autowired
    public ChatEventHandler(SocketIOServer socketIOServer) 
    {
        this.socketIOServer = socketIOServer;
    }
    
    // 클라이언트로부터 'sendMessage' 이벤트를 받았을 때 처리
    public void handleSendMessageEvent(String message) 
    {
        // 받은 메시지를 다른 클라이언트에게 브로드캐스트
        socketIOServer.getBroadcastOperations().sendEvent("receiveMessage", message);
    }
}