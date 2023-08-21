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
	String path;
	int parent_dir_num;
}
