package net.ringo.ringoSwap.domain.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomThumbnail 
{
	int chatroom_num;
	String title;
	String inputdate;
	String message;
}