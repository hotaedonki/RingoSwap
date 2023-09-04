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
	
	public String toString()
	{
		return String.format("multi_num : {%d} / user_num : {%d} / playdate : {%s} / multi_record : {%s} / score : {%d} / category : {%s}"
				,multi_num , user_num, playdate, multi_record, score, category);
	}
}
