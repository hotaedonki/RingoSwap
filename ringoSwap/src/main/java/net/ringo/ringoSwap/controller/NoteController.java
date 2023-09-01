package net.ringo.ringoSwap.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Directory;
import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirNotepad;
import net.ringo.ringoSwap.domain.DirWord;
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

	//<<<<<<<<<<<<<<<-------------[ 노트 출력기능 시작 ]--------------
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
	//-----------[ 노트 출력기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 생성기능 시작 ]-----------------------
	//html에서 받은 정보를 기반으로 directory객체를 생성하는 기능을 가진 메서드
	@ResponseBody
	@PostMapping("dirCreate")
	public void dirCreate(@AuthenticationPrincipal UserDetails user
					, String dir_name, @RequestParam(name="parent_dir_name", defaultValue = "-1") int parent_dir_num){
		//접속한 사용자의 id로 user_num값 획득
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		//폴더 생성을 위한 정보를 담을 Directory 객체 생성
		Directory dir = new Directory();
		dir.setDir_name(dir_name);
		dir.setUser_num(user_num);
		dir.setParent_dir_num(parent_dir_num);
		
		int num = service.dirCreateOne(dir);
	}
	/*
	 * html에서 받은 정보를 기반으로 file객체를 생성하는 기능을 가진 메서드
	 * 지정된 분류에 따라 추가로 생성하는 객체가 Notepad인지 Word인지 if문으로 구분된다.
	 * 
	 */
	@ResponseBody
	@PostMapping("fileCreateOne")
	public void fileCreate(@AuthenticationPrincipal UserDetails user
					,int dir_num, String title, String file_type) {
		//접속한 사용자의 id로 user_num값 획득
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		//파일 생성을 위한 정보를 xml까지 가져갈 DirFile 매개변수 생성
		DirFile file = new DirFile();
		//file에 생성을 위한 정보 삽입
		file.setUser_num(user_num);
		file.setDir_num(dir_num);
		file.setTitle(title);
		file.setFile_type(file_type);				//값이 notepad일경우 메모장 생성, word일경우 단어장 본체 생성
		//파일 생성
		int num = service.fileCreateOne(file);
		int file_num = 0;//그 방법을 알아서 생성과 동시에 file_num을 받아오는 시스템을 구현할 것
		
		//file_type에 따라 추가생성 정보 분류
		if(file_type.equals("note")) {
			DirNotepad note = new DirNotepad();
			note.setDir_num(dir_num);
			note.setFile_num(file_num);
			note.setUser_num(user_num);
			
			int no2 = service.notepadCreateOne(note);
		}else if(file_type.equals("word")) {
		}
		
	}

	//html에서 받은 정보를 기반으로 특정 file객체에 종속되는 word객체를 생성하는 기능을 가진 메서드
	@ResponseBody
	@PostMapping("wordCreate")
	public void wordCreate(@AuthenticationPrincipal UserDetails user
					, int file_num, String word, String pron, String mean) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		DirWord inputword = new DirWord();
		inputword.setFile_num(file_num);
		inputword.setUser_num(user_num);
		inputword.setWord(word);
		inputword.setPron(pron);
		inputword.setMean(mean);
		
		int num = service.wordCreateOne(inputword);
	}
	//-----------[ 노트 생성기능 종료 ]-------------->>>>>>>>>>>>>>
	
	//<<<<<<<<<<<<-----[ 노트 삭제기능 시작 ]-----------------------
	//-----------[ 노트 삭제기능 종료 ]-------------->>>>>>>>>>>>>>
}