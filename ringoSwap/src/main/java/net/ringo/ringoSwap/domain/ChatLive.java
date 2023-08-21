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
}
