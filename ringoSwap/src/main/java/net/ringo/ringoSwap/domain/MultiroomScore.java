package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiroomScore {
	int multi_num;
	int user_num;
	String playdate;
	String multi_record;
	int score;
	String category;
}
