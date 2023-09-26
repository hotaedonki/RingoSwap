package net.ringo.ringoSwap.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.GameSetting;
import net.ringo.ringoSwap.domain.SingleDifficulty;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.GameService;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.service.NoteService;

@Slf4j
@Controller
@RequestMapping("game")
public class GameController 
{
	@Autowired
	GameService service;
	@Autowired
	MemberService memberService;
	@Autowired
	NoteService noteService;
	//게임서비스의 메인페이지로 이동하는 컨트롤러 메서드
	@GetMapping("gameMain")
	public String gameMain()
	{
		return "game/gameMain";
	}
	
	
	//이하 싱글게임 진입 메서드 목록

	@GetMapping("MCQ")
	public String MCQ()
	{
		return "game/MCQ";
	}

	@GetMapping("flashCards")
	public String flashCards()
	{
		return "game/flashCards";
	}
	@GetMapping("dictation")
	public String dictation()
	{
		return "game/dictation";
	}
	
	// 해당 사용자가 생성한 단어장분류 파일 목록을 user_num을 매개변수로 검색하여 리턴하는 메서드
	@ResponseBody
	@PostMapping("fileOpenWordNote")
	public ArrayList<DirFile> fileOpenWordNote(@AuthenticationPrincipal UserDetails user) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		//회원번호를 매개변수로 file_type이 word인 파일목록을 리턴하는 메서드 실행
		ArrayList<DirFile> note = service.wordFileSelectByUserNum(user_num);

		log.debug("파일 열어~~~: {} ", note);
		return note;
	   }
	//해당 사용자가 지정한 단어장 분류 파일의 파일번호를 매개변수로 DB를 수정하는 메서드
	@ResponseBody
	@PostMapping("fileWordUpdate")
	public int fileWordUpdate(int file_num, @AuthenticationPrincipal UserDetails user) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		
		int methodResult = service.fileWordUpdate(file_num, user_num);
		
		return methodResult;
	}
	//해당 사용자의 게임 설정을 불러오는 메서드
	@ResponseBody
	@PostMapping("gameSettingOpen")
	public GameSetting gameSettingOpen(@AuthenticationPrincipal UserDetails user) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		GameSetting setting = service.gameSettingSelectByUserNum(user_num);
		return setting;
	}
	//해당 사용자의 게임설정을 업데이트하는 메서드
	@ResponseBody
	@PostMapping("gameSettingUpdate")
	public int gameSettingOpen(GameSetting setting
					, @AuthenticationPrincipal UserDetails user) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		setting.setUser_num(user_num);
		log.debug("세팅값 : {}", setting);
		int methodResult = service.gameSettingUpdate(setting);
		
		return methodResult;

	}
	
	//사용자가 설정한 문제갯수 설정, 받아쓰기에 발음을 사용하는지 여부등의 세팅값을 출력하는 메서드
	@ResponseBody
	@PostMapping("gameSettingPrint")
	public GameSetting gameSettingPrint(@AuthenticationPrincipal UserDetails user) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		GameSetting setting = service.gameSettingSelectByUserNum(user_num);
		log.debug("세팅 {}", setting);
		return setting;
	}
	//사용자가 설정한 문제갯수 설정을 업데이트하는 메서드
	@ResponseBody
	@PostMapping("gameSettingUpdateQuestionNum")
	public int gameSettingUpdateQuestionNum(@AuthenticationPrincipal UserDetails user
					, Integer question_num) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		HashMap<String, Object> map = new HashMap<>();
		
		if(question_num == null) {
			question_num = 0;
		}
		
		map.put("user_num", user_num);
		map.put("question_num", question_num);
		
		int methodResult = service.gameSettingUpdateQuestionNum(map);
		return methodResult;
	}

	@ResponseBody
	@PostMapping("matchUseUpdate")
	public int matchUseUpdate(boolean match_use
					, @AuthenticationPrincipal UserDetails user) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		HashMap<String, Object> map = new HashMap<>();
		map.put("match_use", match_use);
		map.put("user_num", user_num);
		int methodResult = service.matchUseUpdate(map);
		return methodResult;
	}
	//-------------------------------------------------->>>>

	
	//<<<<-------------------------------------------------
	@ResponseBody
	@PostMapping("gameNotePrint")
	public HashMap<String, Object> gameNotePrint(@AuthenticationPrincipal UserDetails user
			, String category){
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		HashMap<String, Object> map = new HashMap<>();		//리턴용 변수
		//회원번호를 매개변수로 사용자의 게임세팅 정보를 리턴
		GameSetting setting = service.gameSettingSelectByUserNum(user_num);		
		
		if(setting.getFile_num() == -1) {
			map.put("setting", setting);
			return map;	//file_num이 설정되어있지 않을 경우, 게임실행이 불가하기에 null값을 리턴
		}
		//회원정보에 기록된 file_num을 매개변수로 해당 단어장 정보를 리턴
		ArrayList<DirWord> wordList = service.wordArraySearchByGameSetting(setting);
		
		if(category.equals("MCQ")) {
			// 2.1. 랜덤 단어 선택
			Random random = new Random();
		    int index = random.nextInt(wordList.size());
		    DirWord selectedWord = wordList.get(index);
		    // 2.2. 정답 설정
		    String correctAnswer = selectedWord.getMean();
		    // 2.3. 오답 설정
		    Set<String> wrongAnswers = new HashSet<>();
		    while (wrongAnswers.size() < 3) {
		        int wrongIndex = random.nextInt(wordList.size());
		        if (wrongIndex != index) {
		            wrongAnswers.add(wordList.get(wrongIndex).getMean());
		        }
		    }
		    // 2.4. 4개의 의미를 무작위 순서로 배치
		    List<String> options = new ArrayList<>(wrongAnswers);
		    options.add(correctAnswer);
		    Collections.shuffle(options);
		}
		map.put("setting", setting);
		map.put("wordList", wordList);
		log.debug(null);
		
		return map;
	}
}