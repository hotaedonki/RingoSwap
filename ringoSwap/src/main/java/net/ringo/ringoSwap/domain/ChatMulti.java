package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMulti {
	int multi_chat_num;
	int user_num;
	int multi_num;
	String message;
	String inputdate;
	
	public String toString()
	{
		return String.format("multi_chat_num : {%d} / user_num : {%d} / multi_num : {%d} / message : {%s} / inputdate : {%s}"
				, multi_chat_num, user_num, multi_num, message, inputdate);
	}
}
