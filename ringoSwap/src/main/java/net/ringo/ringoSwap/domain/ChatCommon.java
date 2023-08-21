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
}
