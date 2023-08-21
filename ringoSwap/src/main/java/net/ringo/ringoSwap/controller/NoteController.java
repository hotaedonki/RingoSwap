package net.ringo.ringoSwap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("note")
public class NoteController 
{
	@GetMapping("noteMain")
	public String noteMain()
	{
		return "note/noteMain";
	}
}