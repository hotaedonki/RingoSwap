package net.ringo.ringoSwap.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Member;
import net.ringo.ringoSwap.service.EmailService;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.util.PathHandler;

@Slf4j
@Controller
@RequestMapping(PathHandler.MEMBER)
public class MemberController 
{
	@Autowired
	MemberService service;
	
	@Autowired
	EmailService emailService;
	
	@GetMapping(PathHandler.JOIN)
	public String join()
	{
		return "memberView/join";
	}
	
	@PostMapping(PathHandler.JOIN)
	public String join(Member m)
	{	
		log.debug("[ Join... ]");
		log.debug(m.toString());
		
		service.insertMember(m);
		
		return "main";
	}
	
	@GetMapping("login")
	public String login(String error, Model model)
	{
		model.addAttribute("error", error);
		
		return "memberView/loginForm";
	}
	
	@PostMapping("login")
	public String login(Member m, Model model) {
		log.debug("login : {}", m);
		Member mCheck = service.memberLogin(m);

		if(mCheck == null) {
			String error = "등록되지 않은 계정입니다.";
			model.addAttribute("error", error);
			return "redirect:/member/login";
		}
		if(!mCheck.isEnabled()) {
			String error = "인증되지 않은 회원입니다.";
			model.addAttribute("error", error);
			return "redirect:/member/login";
		}
		return "main";
	}
	
	@ResponseBody
	@PostMapping(PathHandler.EMAILCONFIRM)
	public void emailConfirm(String email) throws Exception
	{
		String confirm = emailService.sendSimpleMessage(email);
	}
}