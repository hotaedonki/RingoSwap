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
	/*
	 * 멀티 사천성 게임 페이지 진입 시퀸스
	 * 1.게임 플레이 버튼 클릭시 '다른 참가자들을 모으는 중입니다...'라는 메세지를 띄우는 로딩창을 출력한다.
	 * 2.1의 이벤트를 처리하면서 사용자가 클릭한 멀티게임의 참가자를 모은다.
	 * 3.참가자가 일정이상 모이면 해당 게임을 시작할 수 있는 게임방으로 이동한다.
	 */
	@GetMapping("multiSichuan")
	public String multiSichuan(@AuthenticationPrincipal UserDetails user) {
		
		return "game/multiSichuan";
	}
	//멀티 끝말잇기 게임 페이지
	@GetMapping("multiSiritory")
	public String multiSiritory(@AuthenticationPrincipal UserDetails user) {
		
		return "game/multiSiritory";
	}
	//멀티 스무고개 게임 페이지
	@GetMapping("multiTwenty")
	public String multiTwenty(@AuthenticationPrincipal UserDetails user) {
		
		return "game/multiTwenty";
	}
	//멀티 캐치마인드 게임 페이지
	@GetMapping("multiCatch")
	public String multiCatch(@AuthenticationPrincipal UserDetails user) {
		
		return "game/multiCatch";
	}
}