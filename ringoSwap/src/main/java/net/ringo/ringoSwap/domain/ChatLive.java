package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatLive {
	int live_chat_num;
	int user_num;
	int live_num;
	String message;
	String inputdate;

	public String toString()
	{
		return String.format("live_chat_num : {%d} / user_num : {%d} / live_num : {%d} / message : {%s} / inputdate : {%s}"
				, live_chat_num, user_num, live_num, message, inputdate);
	}
}
