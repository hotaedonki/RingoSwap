package net.ringo.ringoSwap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("game")
public class GameController 
{
	@GetMapping("gameMain")
	public String gameMain()
	{
		return "game/gameMain";
	}
}