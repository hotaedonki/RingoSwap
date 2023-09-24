package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSetting {
	int user_num;
	int file_num;
	String form_type;
	String word_type;
	boolean description_show;
	boolean pron_show;
	int question_num;
	String answer_category;
	
	public String toString()
	{
		return String.format("user_num : {%d} / file_num : {%d} / question_num : {%d} / form_type : {%s} / word_type : {%s} / description_show : {%b} / pron_show : {%b} / answer_category : {%s}"
				, user_num, file_num, question_num, form_type, word_type, description_show, pron_show, answer_category);
	}
}
