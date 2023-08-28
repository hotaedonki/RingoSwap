package net.ringo.ringoSwap.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

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
import net.ringo.ringoSwap.domain.Directory;
import net.ringo.ringoSwap.dao.Mapper;
import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirNotepad;
import net.ringo.ringoSwap.domain.DirWord;
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
	public String noteMain()
	{
		
		return "note/noteMain";
	}

	@ResponseBody
	@PostMapping("dirPrint")
	public ArrayList<Directory> dirPrint(@AuthenticationPrincipal UserDetails user) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		ArrayList<Directory> dirList = service.selectUserDirectoryAll(user_num);

		log.debug("디렉토리 : {}", dirList);
		
		return dirList;
	}
	
	@ResponseBody
	@PostMapping("filePrint")
	public ArrayList<DirFile> filePrint(@AuthenticationPrincipal UserDetails user
					, String category, String sort, String text) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		HashMap<String, Object> map = new HashMap<>();
		map.put("user_num", user_num);
		map.put("category", category);
		map.put("sort", sort);
		map.put("text", text);
		
		ArrayList<DirFile> fileList = service.selectUserFileAll(map);
		
		log.debug("파일 : {}", fileList);
		
		return fileList;
	}
	
	//ajax를 통해 실행되는 하위 폴더를 검색하여 그 목록을 리턴하는 메서드
	@ResponseBody
	@PostMapping("dirOpenDirectory")
	public ArrayList<Directory> dirOpenDirectory(int dir_num){
		ArrayList<Directory> dirList = service.selectDirectoryByPDirNum(dir_num);
		return dirList;
	}
	//ajax를 통해 실행되는 하위 파일을 검색하여 그 목록을 리턴하는 메서드
	@ResponseBody
	@PostMapping("dirOpenFile")
	public ArrayList<DirFile> dirOpenFile(int dir_num, String category, String sort, String text){
		HashMap<String, Object> map = new HashMap<>();
		map.put("dir_num", dir_num);
		map.put("category", category);
		map.put("sort", sort);
		map.put("text", text);
		ArrayList<DirFile> fileList = service.selectFileByDirNum(map);
		return fileList;
	}
	
	//ajax를 통해 실행되는 해당 메모장분류 파일을 file_num을 매개변수로 검색하여 그 하위 Notepad객체를 리턴하는 메서드
	@ResponseBody
	@PostMapping("fileOpenNote")
	public DirNotepad fileOpenNote(int file_num) {
		String title = service.selectFileByFileNumReturnTitle(file_num);
		DirNotepad notepad = service.selectNotepadByFileNum(file_num);
		
		notepad.setTitle(title);
		
		log.debug("ghkrdls: {} ", notepad);
		return notepad;
	}
	
	//ajax를 통해 실행되는 해당 메모장분류 파일을 file_num을 매개변수로 검색하여 그 하위 Word목록을 리턴하는 메서드
	@ResponseBody
	@PostMapping("fileOpenWord")
	public ArrayList<DirWord> fileOpenWord(int file_num) {
		ArrayList<DirWord> wordList = service.selectWordArrayByFileNum(file_num);
		log.debug("ghkrdls: {} ", wordList);
		return wordList;
	}
	
}