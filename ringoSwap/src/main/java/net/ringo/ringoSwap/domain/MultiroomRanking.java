package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiroomRanking {
	int user_num;
	String category;
	int score;
	int maxscore;
	String last_playtime;
}
