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
	
	public String toString()
	{
		return String.format("chatroom_num : {%d} / host_num : {%d} / title : {%s} / lang_category : {%s} / gen_category : {%s} / modifie_date : {%s} / totalsize : {%d}"
				, chatroom_num, host_num, title, lang_category, gen_category, modifie_date, totalsize);
	}
}
