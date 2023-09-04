package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Multiroom {
	int multi_num;
	int host_num;
	String multi_title;
	String opendate;
	int capacity;
	String category;
	
	public String toString()
	{
		return String.format("multi_num : {%d} / host_num : {%d} / multi_title : {%s} / opendate : {%s} / capacity : {%d} / category : {%s}"
				, multi_num, host_num, multi_title, opendate, capacity, category);
	}
}
