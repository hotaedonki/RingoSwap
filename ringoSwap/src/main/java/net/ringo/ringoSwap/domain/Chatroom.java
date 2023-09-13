package net.ringo.ringoSwap.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ringo.ringoSwap.service.ChatService;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chatroom {
	int chatroom_num;
	int host_num;
	String title;
	String lang_category;
	String gen_category;
	String modifie_date;
	int totalsize;
	private Set<WebSocketSession> sessions = new HashSet<>();
	
	public void handleAction(WebSocketSession session, ChatCommon message, ChatService service)
	{
        //message 에 담긴 타입을 확인한다.
        //이때 message 에서 getType 으로 가져온 내용이
        //ChatCommon 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면
        if (message.getType().equals(ChatCommon.MessageType.ENTER))
        {
            //sessions 에 넘어온 session 을 담고,
            sessions.add(session);

            //message 에는 입장하였다는 메시지를 띄워줍니다.
            message.setMessage(message.getUser_num() + " 님이 입장하였습니다.");
            sendMessage(message,service);
        } 
        else if (message.getType().equals(ChatCommon.MessageType.TALK)) 
        {
            message.setMessage(message.getMessage());
            sendMessage(message,service);
        }
	}
	
	public void sendMessage(ChatCommon message, ChatService service)
	{
        sessions.parallelStream().forEach(sessions -> service.sendMessage(sessions, message));
	}
	
	public String toString()
	{
		return String.format("chatroom_num : {%d} / host_num : {%d} / title : {%s} / lang_category : {%s} / gen_category : {%s} / modifie_date : {%s} / totalsize : {%d}"
				, chatroom_num, host_num, title, lang_category, gen_category, modifie_date, totalsize);
	}
}
