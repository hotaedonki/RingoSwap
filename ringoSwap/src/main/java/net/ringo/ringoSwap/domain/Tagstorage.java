package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tagstorage {
	int tag_num;
	String tag_name;
	char tag_bann;
	
	public String toString()
	{
		return String.format("tag_num : {%d} / tag_name : {%s} / tag_bann : {%c}"
				, tag_num, tag_name, tag_bann);
	}
}
