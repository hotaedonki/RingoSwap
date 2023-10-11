package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameLog{
	int gamelog_num;				//게임로그 번호를 기록하는 기본키 변수
	int user_num;					//싱글게임을 실행하는 유저의 계정 번호를 저장하는 왜래키
	int file_num;						//문제제출에 사용하는 단어장 번호를 저장하는 변수
	String file_title;					//게임에 사용한 단어장의 제목
	String game_category;		//게임종류
	String lang_category;			//게임의 언어종류
	double score;							//정답률을 기록하는 변수
	int point;							//해당 게임으로 획득한 포인트
	String inputdate;				//해당 게임을 실시한 일자 기록
	
	public String toString()
	{
		return String.format("gamelog_num : {%d} / user_num : {%d} / file_num : {%d} / file_title : {%s} / "
				+ "game_category : {%s} / lang_category : {%s} / score : {%f} / point : {%d} / inputdate : {%s}"
				,gamelog_num, user_num, file_num, file_title, game_category, lang_category, score, point, inputdate);
	}
}
