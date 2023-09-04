package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleDifficulty {
	int user_num;					//싱글게임을 실행하는 유저의 계정 번호를 저장하는 왜래키 겸 기본키 변수
	int file_num;						//문제제출에 사용하는 단어장 번호를 저장하는 변수
	String difficulty;					//문제 난이도, 'hard', 'average', 'easy'
	String method;					//문제 배열 방법
	int quest_num;					//한 게임당 문제 갯수, 사천성 게임에는 적용되지 않는다.
	int sichuan_num;				//사천성 게임에서 생성되는 카드를 몇개로 할지 지정하는 변수. select-option으로 일정 갯수 범위로 지정한다.
	boolean check;					//체크한 단어만 문제에 사용할지 여부를 저장하는 변수. true=활성화, false=비활성화
	String answer_category;	//문제의 정답을 단어, 발음, 뜻 3가지 중 어떤 것으로 받을지 지정하는 변수. 발음&뜻으로 정답을 받게 할 수도 있다.
	
	public String toString()
	{
		return String.format("user_num : {%d} / file_num : {%d} / difficulty : {%s} / method : {%s} / quest_num : {%d} / sichuan_num : {%d} / check : {%b} / answer_category : {%s}"
				, user_num, file_num, difficulty, method, quest_num, sichuan_num, check, answer_category);
	}
}
