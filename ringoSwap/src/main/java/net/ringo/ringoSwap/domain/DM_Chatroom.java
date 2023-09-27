package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DM_Chatroom 
{
	int dm_chatroom_num;
	int user_num1;
	int user_num2;
	String modifie_date;
	int totalsize;
	
	public String toString()
	{
		return String.format("dm_chatroom_num : {%d} / user_num1 : {%d} / user_num2 : {%d} / modifie_date : {%s} / totalsize : {%d}"
				, dm_chatroom_num, user_num1, user_num2, modifie_date, totalsize);
	}
}