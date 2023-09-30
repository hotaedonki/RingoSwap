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
import net.ringo.ringoSwap.domain.GameSetting;
import net.ringo.ringoSwap.domain.SingleDifficulty;

@Service
@Slf4j
public class GameServiceImple implements GameService{
	@Autowired
	GameDAO dao;
	
	@Override
	public SingleDifficulty difficultyCall(String user_id) {
		return dao.difficultyCall(user_id);
	}

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
}
