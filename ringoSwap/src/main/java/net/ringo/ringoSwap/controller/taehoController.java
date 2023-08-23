package net.ringo.ringoSwap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("taeho")
public class taehoController {
	
	@GetMapping("main")
	public String main() {
		return "taeho/main";
	}
	
	@GetMapping("login")
	public String login() {
		return "taeho/login";
	}
	
	@PostMapping("join")
	public String join() {
		return "taeho/login";
	}

	
	
}

