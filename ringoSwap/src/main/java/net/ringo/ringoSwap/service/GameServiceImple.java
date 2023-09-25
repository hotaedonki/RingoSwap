package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.dao.GameDAO;
import net.ringo.ringoSwap.domain.DirFile;
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
	public int fileWordUpdate(int file_num, int user_num) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("file_num", file_num);
		map.put("user_num", user_num);
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
	
}
