package net.ringo.ringoSwap.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.util.PathHandler;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class SpringConfig implements WebSocketMessageBrokerConfigurer
{ 
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) 
	{
		// 최초에 한번 실행 후 이 EndPoint를 지정해줌
		// stomp 접속 url -> /ws-stomp
		/*
		 STOMP 란?
			STOMP는 Simple Text Oriented Messaging Protocol의 약자로 메시지 전송을 위한 프로토콜 입니다.
			기본적인 WebSocket 과 가장 크게 다른 점은 기존의 WebSocket 만을 사용한 통신은 발신자와 수신자를 
			Spring 단에서 직접 관리를 해야만 했습니다. 즉, webSocketHandler 를 만들어서 WebSocket 통신을 하는 사용자들을 
			저장하고 이를 직접 관리하며 클라이언트에서 들어오는 메시지를 다른 사용자에게 전달하는 코드를 직접 구현해야만 했습니다.
			하지만 STOMP는 다릅니다. STOMP 는 pub/sub 기반으로 동작하기 때문에 메시지의 송,수신에 대한 처리를 
			명확하게 정의 할 수 있습니다. 이말인 즉슨 추가적으로 코드 작업할 필요 없이 @MessagingMapping 같은 어노테이션을 
			사용하여 메시지 발행 시 엔드포인트만 조정해줌으로써 훨씬 쉽게 메시지 전송/수신이 가능합니다. 
		*/
		
		registry.addEndpoint(PathHandler.WS_STOMP)
				.withSockJS();
		
		
		registry.addEndpoint("/ws-stomp/chat")
				.withSockJS();
	    
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) 
	{
		// 메시지를 구독하는 요청 url -> 메시지를 받을 때
        registry.enableSimpleBroker("/sub");
        // 메시지를 발행하는 요청 url -> 메시지를 보낼 때
        registry.setApplicationDestinationPrefixes("/pub");
	}
}