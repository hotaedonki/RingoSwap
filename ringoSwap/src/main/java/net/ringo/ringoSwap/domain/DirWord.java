package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirWord {
	//단어장 분류의 DirFile에 입력되는 각 단어 정보를 저장하는 ringo_word 테이블과 관련있는 VO객체
	int word_num;
	int file_num;
	int user_num;
	String word;
	String pron;
	String mean;
	String description;
	String inputdate;
	
	public String toString()
	{
		return String.format("word_num : {%d} / file_num : {%d} / user_num : {%d} / word : {%s} / pron : {%s} / mean : {%s} / description : {%s} / inputdate : {%s}"
				, word_num, file_num, user_num, word, pron, mean, description, inputdate);
	}
}
