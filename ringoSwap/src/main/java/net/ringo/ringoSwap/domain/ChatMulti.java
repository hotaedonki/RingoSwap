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
}
