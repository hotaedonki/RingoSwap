package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chatroom {
	int chatroom_num;
	int host_num;
	String title;
	String lang_category;
	String gen_category;
	String modifie_date;
	int totalsize;
}
