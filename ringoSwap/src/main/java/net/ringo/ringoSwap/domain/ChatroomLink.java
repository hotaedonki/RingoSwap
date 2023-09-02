package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomLink 
{
	int user_num;
	int chatroom_num;
	
	public String toString()
	{
		return String.format("user_num : {%d} / chatroom_num : {%d}", user_num, chatroom_num);
	}
}