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
}
