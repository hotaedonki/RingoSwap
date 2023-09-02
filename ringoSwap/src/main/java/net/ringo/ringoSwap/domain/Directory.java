package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Directory {
	int dir_num;
	int user_num;
	String dir_name;
	int parent_dir_num;
	
	public String toString()
	{
		return String.format("dir_num : {%d} / user_num : {%d} / dir_name : {%s} / parent_dir_num: {%d}"
				, dir_num, user_num, dir_name, parent_dir_num);
	}
}
