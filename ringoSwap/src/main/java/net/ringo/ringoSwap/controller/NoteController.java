package net.ringo.ringoSwap.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Directory;
import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.service.NoteService;

@Slf4j
@Controller
@RequestMapping("note")
public class NoteController 
{
	@Autowired
	MemberService memberService;
	@Autowired
	NoteService service;
	
	//노트서비스의 메인페이지로 이동하는 컨트롤러 메서드
	@GetMapping("noteMain")
	public String noteMain(@AuthenticationPrincipal UserDetails user
					, Model model)
	{
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		ArrayList<Directory> dirList = service.selectUserDirectoryAll(user_num);
		ArrayList<DirFile> fileList = service.selectUserFileAll(user_num);
		
		model.addAttribute("dir", dirList);
		model.addAttribute("file", fileList);
		
		return "note/noteMain";
	}
}