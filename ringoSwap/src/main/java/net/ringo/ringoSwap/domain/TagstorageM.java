package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagstorageM {
	int tag_num;
	String tag;
	
	public String toString()
	{
		return String.format("tag_num : {%d} / tag : {%s}"
				, tag_num, tag);
	}
}
