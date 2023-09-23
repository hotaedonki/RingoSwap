package net.ringo.ringoSwap.controller;

import java.util.ArrayList;

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
		//난이도 정보 가져오기
		
		return "game/MCQ";
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
}