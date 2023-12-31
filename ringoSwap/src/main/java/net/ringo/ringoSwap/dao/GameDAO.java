package net.ringo.ringoSwap.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.GameLog;
import net.ringo.ringoSwap.domain.GameSetting;


@Mapper
public interface GameDAO 
{
	/*
	 * user_id를 매개변수로 가져가, 해당 사용자의 싱글게임 난이도 설정 정보를 select로 검색하여
	 * 난이도 정보를 리턴값으로 받아오는 메서드.
	 */

	//user_num을 매개변수로 file_type이 word인 파일목록을 리턴하는 메서드
	ArrayList<DirFile> wordFileSelectByUserNum(int user_num);
	//user_num과 file_num을 매개변수로 DB에서 특정 사용자의 file_num값을 수정하는 메서드
	int fileWordUpdate(HashMap<String, Object> map);
	//user_num을 매개변수로 DB의 게임세팅 정보를 리턴하는 메서드
	GameSetting gameSettingSelectByUserNum(int user_num);
	//사용자가 수정한 게임세팅 정보를 매개변수로  DB의 정보를 수정하는 메서드
	int gameSettingUpdate(GameSetting setting);
	//사용자가 수정한 정보를 매개변수로 DB의 문제갯수 속성을 수정하는 메서드
	int gameSettingUpdateQuestionNum(HashMap<String, Object> map);
	//DB 내부 게임설정의 match_use값을 변경하는 메서드
	int matchUseUpdate(HashMap<String, Object> map);
	//게임세팅 정보값을 매개변수로 해당 세팅에 따라 정렬된 단어목록을 리턴하는 메서드
	ArrayList<DirWord> wordArraySearchByGameSetting(GameSetting setting);
	//사용자의 오답노트를 DB에서 검색해 출력하는 메서드
	ArrayList<DirWord> wordWrongArraySearchByUserNum(GameSetting setting);
	//오답노트에 해당 오답이 존재하는지 확인하는 메서드
	int wordWrongSearch(int word_num);
	//오답을 오답노트에 입력하는 메서드
	int wordWrongArrayInsert(DirWord word);
	//단어목록을 오답노트에서 삭제하는 메서드
	int wordWrongArrayDelete(List<DirWord> rightWordList);

	//<<---------------------------[게임로그 기능 시작]-------------------------------
	//사용자의 user_num을 매개변수로 해당 사용자의 게임로그 목록을 출력하는 메서드
	ArrayList<GameLog> gameLogSearchByUserNum(int user_num);
	//입력받은 게임로그 정보를 DB에 입력하는 메서드
	int gameLogInsert(GameLog gameLog);
	//획득한 포인트를 사용자의 포인트 점수에 기입하는 메서드
	int memberUpdatePoint(HashMap<String, Integer> map);
	
}