package net.ringo.ringoSwap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
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
}