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
	String modifie_date;
	int totalsize;
	int capacity;
	
	public String toString()
	{
		return String.format("chatroom_num : {%d} / host_num : {%d} / title : {%s} / lang_category : {%s} / modifie_date : {%s} / totalsize : {%d} / capacity : {%d}"
				, chatroom_num, host_num, title, lang_category, modifie_date, totalsize, capacity);
	}
}
