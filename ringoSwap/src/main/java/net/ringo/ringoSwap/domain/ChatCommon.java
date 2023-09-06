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
	String message;
	String inputdate;
	String origin_file;
	String saved_file;
	int photo_size;
	
	public String toString()
	{
		return String.format("chat_num : {%d} / user_num : {%d} / chatroom_num : {%d} / photo_size : {%d} / message : {%s} / inputdate : {%s} / origin_file : {%s} / saved_file : {%s}"
				, chat_num, user_num, chatroom_num, photo_size, message, inputdate, origin_file, saved_file);
	}
}
