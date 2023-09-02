package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatCommon {
	int chat_num;
	int user_num;
	int chatroom_num;
	int chatphoto_num;
	String message;
	String inputdate;
	
	public String toString()
	{
		return String.format("chat_num : {%d} / user_num : {%d} / chatroom_num : {%d} / chatphoto_num : {%d} / message : {%s} / inputdate :{%s}"
				, chat_num, user_num, chatroom_num, chatphoto_num, message, inputdate);
	}
}
