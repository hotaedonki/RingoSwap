package net.ringo.ringoSwap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Member;
import net.ringo.ringoSwap.service.MemberService;

@Slf4j
@Controller
@RequestMapping("member")
public class MemberController 
{
	@Autowired
	MemberService service;
	
	@GetMapping("join")
	public String join()
	{
		return "memberView/join";
	}
	
	@PostMapping("join")
	public String join(Member m)
	{	
		log.debug("[ Join... ]");
		log.debug(m.toString());
		
		service.insertMember(m);
		
		return "main";
	}
}