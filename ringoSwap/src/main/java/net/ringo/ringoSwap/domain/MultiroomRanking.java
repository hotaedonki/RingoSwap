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
	
	public String toString()
	{
		return String.format("user_num : {%d} / category : {%s} / score : {%d} / maxscore : {%d} / last_playtime : {%s}"
				, user_num, category, score, maxscore, last_playtime);
	}
}
