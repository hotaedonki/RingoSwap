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
	
	@GetMapping("loginForm")
	public String memberLogin()
	{
		return "memberView/loginForm";
	}
	
	@GetMapping("idCheck")
	public String idCheck(String user_id, Model model)
	{
		//service부의 id체크 메서드 실행
		int n = service.idCheck(user_id);
		if(n==0) {
			model.addAttribute("result", user_id+"는 이미 존재하는 ID입니다.");
		}else {
			model.addAttribute("result", user_id+"는 사용 가능한 ID입니다.");
		}
		
		return "memberView/idCheck";
	}
	
	@ResponseBody
	@PostMapping(PathHandler.EMAILCONFIRM)
	public void emailConfirm(String email) throws Exception
	{
		String confirm = emailService.sendSimpleMessage(email);
	}
}