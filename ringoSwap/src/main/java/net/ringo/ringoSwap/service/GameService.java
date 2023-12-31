package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.GameLog;
import net.ringo.ringoSwap.domain.GameSetting;

public interface GameService {


	//user_num을 매개변수로 file_type이 word인 파일목록을 리턴하는 메서드
	public ArrayList<DirFile> wordFileSelectByUserNum(int user_num);
	//file_num을 매개변수로 DB를 수정하는 메서드
	public int fileWordUpdate(int file_num, String select, int user_num);
	//user_num을 매개변수로 사용자의 게임세팅 정보를 리턴하는 메서드
	public GameSetting gameSettingSelectByUserNum(int user_num);
	//사용자가 수정한 게임세팅 정보를 매개변수로 DB에 업데이트하는 메서드
	public int gameSettingUpdate(GameSetting setting);
	//user_num을 매개변수로 사용자의 게임세팅 정보 중 문제갯수 정보를 리턴하는 메서드
	public int gameSettingSelectByUserNumReturnQuestNum(int user_num);
	//사용자가 수정한 정보를 매개변수로 DB의 문제갯수 속성을 수정하는 메서드
	public int gameSettingUpdateQuestionNum(HashMap<String, Object> map);
	//해당 사용자의 user_num을 매개변수로 DB 내부 게임설정의 match_use값을 변경하는 메서드
	public int matchUseUpdate(HashMap<String, Object> map);

	//사용자의 게임세팅 정보에 따라 정렬된 단어 목록을 리턴하는 메서드
	public ArrayList<DirWord> wordArraySearchByGameSetting(GameSetting setting);
	//사용자의 오답노트를 리턴하는 메서드
	public ArrayList<DirWord> wordWrongArraySearchByUserNum(GameSetting setting);
	//오답을 입력하는 메서드
	public int wordWrongArrayInsert(List<DirWord> wrongWordList);
	//해당 단어목록을 오답노트에서 삭제하는 메서드
	public int wordWrongArrayDelete(List<DirWord> rightWordList);

	//<<---------------------------[게임로그 기능 시작]-------------------------------
	//획득포인트 계산 메서드
	public int gamePointCalcul(Integer rightLength, Integer gameLength);
	//사용자의 user_num을 매개변수로 해당 사용자의 게임로그 목록을 출력하는 메서드
	public ArrayList<GameLog> gameLogSearchByUserNum(int user_num);
	//게임로그를 DB에 입력하는 메서드
	public int gameLogInsert(GameLog gameLog);
}
