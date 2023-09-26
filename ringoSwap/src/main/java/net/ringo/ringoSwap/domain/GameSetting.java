package net.ringo.ringoSwap.domain;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSetting {
	@Column(name="user_num", nullable = false)
	private int user_num;
	@Column(name="file_num", nullable = false)
	private int file_num;
	String form_type;
	String order_type;
	boolean description_show;
	boolean pron_show;
	@Column(name="question_num", nullable = false)
	private int question_num;
	boolean match_use;
	String answer_category;
	
	public String toString()
	{
		return String.format("user_num : {%d} / file_num : {%d} / question_num : {%d} / form_type : {%s} / order_type : {%s} / description_show : {%b} / pron_show : {%b} / match_use : {%b} / answer_category : {%s}"
				, user_num, file_num, question_num, form_type, order_type, description_show, pron_show, match_use, answer_category);
	}
}
