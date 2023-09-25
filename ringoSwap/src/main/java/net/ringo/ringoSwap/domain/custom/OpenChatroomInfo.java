package net.ringo.ringoSwap.domain.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenChatroomInfo 
{
	int chatroom_num;
	String lang_category;
	String title;
	int capacity;
	String modifie_date;
	int host_num;
	String nickname;
}