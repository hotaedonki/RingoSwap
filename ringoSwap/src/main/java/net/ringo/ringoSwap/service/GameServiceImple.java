package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.dao.GameDAO;
import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.GameLog;
import net.ringo.ringoSwap.domain.GameSetting;

@Service
@Slf4j
public class GameServiceImple implements GameService{
	@Autowired
	GameDAO dao;
	

	//user_num을 매개변수로 file_type이 word인 파일목록을 리턴하는 메서드
	@Override
	public ArrayList<DirFile> wordFileSelectByUserNum(int user_num) {
		return dao.wordFileSelectByUserNum(user_num);
	}
	//file_num을 매개변수로 DB를 수정하는 메서드
	@Override
	public int fileWordUpdate(int file_num, String select, int user_num) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("file_num", file_num);
		map.put("select", select);
		map.put("user_num", user_num);
		log.debug("맵 {}", map);
		return dao.fileWordUpdate(map);
	}
	//user_num을 매개변수로 사용자의 게임세팅 정보를 리턴하는 메서드
	@Override
	public GameSetting gameSettingSelectByUserNum(int user_num) {
		return dao.gameSettingSelectByUserNum(user_num);
	}
	//사용자가 수정한 게임세팅 정보를 매개변수로 DB에 업데이트하는 메서드
	@Override
	public int gameSettingUpdate(GameSetting setting) {
		return dao.gameSettingUpdate(setting);
	}
	//user_num을 매개변수로 사용자의 게임세팅 정보 중 문제갯수 정보를 리턴하는 메서드
	@Override
	public int gameSettingSelectByUserNumReturnQuestNum(int user_num) {
		GameSetting setting = dao.gameSettingSelectByUserNum(user_num);
		int methodResult = setting.getQuestion_num();
		log.debug("methodResult {}", methodResult);
		return methodResult;
	}
	//사용자가 수정한 정보를 매개변수로 DB의 문제갯수 속성을 수정하는 메서드
	@Override
	public int gameSettingUpdateQuestionNum(HashMap<String, Object> map) {
		return dao.gameSettingUpdateQuestionNum(map);
	}

	//해당 사용자의 user_num을 매개변수로 DB 내부 게임설정의 match_use값을 변경하는 메서드
	@Override
	public int matchUseUpdate(HashMap<String, Object> map) {
		return dao.matchUseUpdate(map);
	}
	
	
	//사용자의 게임세팅 정보에 따라 정렬된 단어 목록을 리턴하는 메서드
	public ArrayList<DirWord> wordArraySearchByGameSetting(GameSetting setting){
		return dao.wordArraySearchByGameSetting(setting);
	}
	//사용자의 오답노트를 리턴하는 메서드
	@Override
	public ArrayList<DirWord> wordWrongArraySearchByUserNum(GameSetting setting){
		return dao.wordWrongArraySearchByUserNum(setting);
	}
	//오답을 입력하는 메서드
	@Override
	public int wordWrongArrayInsert(List<DirWord> wrongWordList) {
		int methodResult = 0;
		for(DirWord word : wrongWordList) {
			methodResult = dao.wordWrongSearch(word.getWord_num());
			if(methodResult !=0) {
				continue;
			}
			methodResult = dao.wordWrongArrayInsert(word);
		}
		return methodResult;
	}
	//해당 단어목록을 오답노트에서 삭제하는 메서드
	@Override
	public int wordWrongArrayDelete(List<DirWord> rightWordList) {
		int methodResult = 0;
		if(rightWordList != null && !rightWordList.isEmpty()) {
			methodResult = dao.wordWrongArrayDelete(rightWordList);
		}
		return methodResult;
	}

	//<<---------------------------[게임로그 기능 시작]-------------------------------
	//획득포인트 계산 메서드
	@Override
	public int gamePointCalcul(Integer rightLength, Integer gameLength) {
	    if(rightLength == null || gameLength == null) {
	        // Handle the error, e.g., return an error code or throw an exception
	        throw new IllegalArgumentException("rightLength or gameLength cannot be null");
	    }
	    // Check if gameLength is zero to avoid division by zero
	    if(gameLength == 0) {
	        // Handle the error, e.g., return an error code or throw an exception
	        throw new IllegalArgumentException("gameLength cannot be zero");
	    }
	    
		//전체 문제수 X 2p 에 정답률을 곱한 값을 기본 획득 포인트로 계산, 그후 정답 1문제당 +4p, 오답 1문제당 -5p
	    //상기 계산으로 나온 최종값을 / 10 한 정수값을 포인트로 배분한다.
		int defaultPoint = (int)(((float)rightLength / gameLength) * 200);
		int wrongLength = gameLength - rightLength;
		int point = (defaultPoint + (rightLength * 4) - (wrongLength * 5)) / 10;
		return point;
	}
	//사용자의 user_num을 매개변수로 해당 사용자의 게임로그 목록을 출력하는 메서드
	@Override
	public ArrayList<GameLog> gameLogSearchByUserNum(int user_num){
		return dao.gameLogSearchByUserNum(user_num);
	}
	//게임로그를 DB에 입력하는 메서드
	@Override
	public int gameLogInsert(GameLog gameLog) {
		HashMap<String, Integer> map = new HashMap<>();
		
		int methodResult = dao.gameLogInsert(gameLog);
		map.put("user_num", gameLog.getUser_num());
		map.put("point", gameLog.getPoint());
		methodResult = dao.memberUpdatePoint(map);
		return methodResult;
	}
}
