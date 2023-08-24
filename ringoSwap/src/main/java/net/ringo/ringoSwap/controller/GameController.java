package net.ringo.ringoSwap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.SingleDifficulty;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.GameService;

@Slf4j
@Controller
@RequestMapping("game")
public class GameController 
{
	@Autowired
	GameService service;
	//게임서비스의 메인페이지로 이동하는 컨트롤러 메서드
	@GetMapping("gameMain")
	public String gameMain()
	{
		return "game/gameMain";
	}
	
	//난이도 설정 페이지로 이동하는 메서드
	@GetMapping("singleDifficultySelect")
	public String singleDifficultySelect(@AuthenticationPrincipal UserDetails user
						, Model model)
	{
		SingleDifficulty diffi = service.difficultyCall(user.getUsername());
		
		return "game/gameMain";
	}
	
	
	//이하 싱글게임 진입 메서드 목록
	/*
	 * 싱글 사천성 게임 페이지 진입 메서드
	 * difficultyCall메서드를 사용하여 난이도 정보를 불러오고, 해당 정보에 따라 지정된 난이도의 사천성 게임을 해당 페이지에서 실행한다.
	 */
	@GetMapping("singleSichuan")
	public String singleSichuan(@AuthenticationPrincipal UserDetails user
			, Model model)
	{
		//난이도 정보 가져오기
		SingleDifficulty diffi = service.difficultyCall(user.getUsername());
		
		model.addAttribute("diffi", diffi);
		
		return "game/singleSichuan";
	}
	/*
	 * 싱글 단어조합 게임 페이지 진입 메서드
	 * 동일하게 difficultyCall메서드를 사용하여 지정된 난이도의 게임을 해당 페이지에서 실행한다.
	 */
	@GetMapping("singleWordCombine")
	public String singleCatch(@AuthenticationPrincipal UserDetails user
			, Model model)
	{
		//난이도 정보 가져오기
		SingleDifficulty diffi = service.difficultyCall(user.getUsername());
		return "game/singleWordCombine";
	}
	/*
	 * 싱글 객관식 게임 페이지 진입 메서드
	 * 동일하게 difficultyCall메서드를 사용하여 지정된 난이도의 게임을 해당 페이지에서 실행한다.
	 */
	@GetMapping("singleChoice")
	public String singleSiritory(@AuthenticationPrincipal UserDetails user
			, Model model)
	{
		//난이도 정보 가져오기
		SingleDifficulty diffi = service.difficultyCall(user.getUsername());
		return "game/singleChoice";
	}
	/*
	 * 싱글 받아쓰기 게임 페이지 진입 메서드
	 * 동일하게 difficultyCall메서드를 사용하여 지정된 난이도의 게임을 해당 페이지에서 실행한다.
	 */
	@GetMapping("singleDictation")
	public String singleDictation(@AuthenticationPrincipal UserDetails user
			, Model model)
	{
		//난이도 정보 가져오기
		SingleDifficulty diffi = service.difficultyCall(user.getUsername());
		
		return "game/singleDictation";
	}
	
	//이하 멀티게임 진입 메서드 목록
	
}