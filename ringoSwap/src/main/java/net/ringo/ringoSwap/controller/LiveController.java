package net.ringo.ringoSwap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.LiveService;

@Slf4j
@Controller
@RequestMapping("live")
public class LiveController 
{
	@Autowired
	LiveService service;
	//라이브서비스의 메인페이지로 이동하는 컨트롤러 메서드
	@GetMapping("liveMain")
	public String liveMain()
	{
		return "live/liveMain";
	}
}