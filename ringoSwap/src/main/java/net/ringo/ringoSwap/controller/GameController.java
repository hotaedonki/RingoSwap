package net.ringo.ringoSwap.controller;

import java.util.ArrayList;
import java.util.HashMap;

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
import net.ringo.ringoSwap.domain.GameSetting;
import net.ringo.ringoSwap.domain.SingleDifficulty;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.GameService;
import net.ringo.ringoSwap.service.MemberService;

@Slf4j
@Controller
@RequestMapping("game")
public class GameController 
{
	@Autowired
	GameService service;
	@Autowired
	MemberService memberService;
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
		int methodResult = service.gameSettingUpdate(setting);
		
		return methodResult;

	}
	
	//사용자가 설정한 문제갯수 설정을 출력하는 메서드
	@ResponseBody
	@PostMapping("questNumPrint")
	public int questNumPrint(@AuthenticationPrincipal UserDetails user) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		int methodResult = service.gameSettingSelectByUserNumReturnQuestNum(user_num);
		
		return methodResult;
	}
	//사용자가 설정한 문제갯수 설정을 업데이트하는 메서드
	@ResponseBody
	@PostMapping("gameSettingUpdateQuestionNum")
	public int gameSettingUpdateQuestionNum(@AuthenticationPrincipal UserDetails user
					, int question_num) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		HashMap<String, Object> map = new HashMap<>();
		map.put("user_num", user_num);
		map.put("question_num", question_num);
		
		int methodResult = service.gameSettingUpdateQuestionNum(map);
		return methodResult;
	}
}