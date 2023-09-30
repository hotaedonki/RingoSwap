package net.ringo.ringoSwap.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Directory;
import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirPhoto;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.service.NoteService;
import net.ringo.ringoSwap.util.PageNavigator;

@Slf4j
@Controller
@RequestMapping("note")
public class NoteController 
{
	@Autowired
	MemberService memberService;
	@Autowired
	NoteService service;

	//실제 저장하는 저장경로
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;
	//단어장 목록의 페이지당 글 수
	@Value("${user.board.page}")
	int countPerPage;
	//단어장 목록의 페이지 이동 링크 수
	@Value("${user.board.pageGroup}")
	int pagePerGroup;
   
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
      log.debug("폴더 출력, 유저 넘버 : {}", user_num);
      ArrayList<Directory> dirList = service.userDirectorySelectAll(user_num);
      log.debug("폴더 목록 : {}", dirList);
      
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
      
      ArrayList<DirFile> fileList = service.userFileSelectAll(map);
      
      log.debug("파일 : {}", fileList);
      
      return fileList;
   }
   
   //ajax를 통해 실행되는 하위 폴더를 검색하여 그 목록을 리턴하는 메서드
   @ResponseBody
   @PostMapping("dirOpenDirectory")
   public ArrayList<Directory> dirOpenDirectory(int dir_num){
      log.debug("폴더넘버값 {}", dir_num);
      ArrayList<Directory> dirList = service.directorySelectByPDirNum(dir_num);
      log.debug("폴더열기 {}", dirList);
      return dirList;
   }
   //ajax를 통해 실행되는 하위 파일을 검색하여 그 목록을 리턴하는 메서드
   @ResponseBody
   @PostMapping("dirOpenFile")
   public ArrayList<DirFile> dirOpenFile(int dir_num, String sort){
      HashMap<String, Object> map = new HashMap<>();
      map.put("dir_num", dir_num);
      map.put("sort", sort);
      log.debug("파일열기 {}", map);
      ArrayList<DirFile> fileList = service.fileSelectByDirNum(map);
      log.debug("파일열기 {}", fileList);
      return fileList;
   }
   
   //ajax를 통해 실행되는 해당 메모장분류 파일을 file_num을 매개변수로 검색하여 그 하위 Notepad객체를 리턴하는 메서드
   @ResponseBody
   @PostMapping("fileOpenNote")
   public DirFile fileOpenNote(int file_num) {
      DirFile note = service.fileSelectByFileNum(file_num);
      
      log.debug("파일 열어~~~: {} ", note);
      return note;
   }
   //메모장분류 파일 출력후 해당 텍스트에 같이 출력될 사진 데이터를 DB에서 찾아 배열로 리턴하는 메서드
   @ResponseBody
   @PostMapping("fileOpenNotePhoto")
   public ArrayList<DirPhoto> fileOpenNotePhoto(int file_num){
	   ArrayList<DirPhoto> photoList = service.filePhotoSelectByFileNum(file_num);
	   return photoList;
   }
   
   //ajax를 통해 실행되는 해당 메모장분류 파일을 file_num을 매개변수로 검색하여 그 하위 Word목록을 리턴하는 메서드
   @ResponseBody
	@PostMapping("fileOpenWord")
	public HashMap<String, Object> fileOpenWord(int file_num, @RequestParam(name="page", defaultValue ="1" ) int page) {
	   HashMap<String, Object> map = new HashMap<>();			//리턴용 변수 설정
		//해당 단어장에서 사용할 네비게이터 navi 생성
		PageNavigator navi = service.wordSelectPageNavigator(pagePerGroup, countPerPage, page, file_num);
		//navi를 사용해 단어 배열을 리턴하는 메서드 실행
		ArrayList<DirWord> wordList = service.selectWordArrayByFileNum(navi, file_num);
		//리턴받은 객체들을 hashmap에 put하고 리턴
		String title = service.fileSelectByFileNumReturnTitle(file_num);
		map.put("title", title);
		map.put("navi", navi);
		map.put("wordList", wordList);
		log.debug("파일오픈 맵 {}",map);
		
		return map;
	}
   
   //가장 최근에 작성한 메모장/단어장을 출력하는 메서드
   @ResponseBody
	@PostMapping("fileOpenLatestOne")
   public DirFile fileOpenLatestOne(@AuthenticationPrincipal UserDetails user) {
	   int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
	   DirFile file = service.fileSelectByModifieDate(user_num);
	   if(file.equals(null)) {
		   file.setFileIsNone(1);
	   }
	   
	   return file;
   }
   //-----------[ 노트 출력기능 종료 ]-------------->>>>>>>>>>>>>>

   //<<<<<<<<<<<<-----[ 노트 생성기능 시작 ]-----------------------
   //html에서 받은 정보를 기반으로 directory객체를 생성하는 기능을 가진 메서드
   @ResponseBody
   @PostMapping("dirCreate")
   public void dirCreate(@AuthenticationPrincipal UserDetails user
               , String dir_name, int parent_dir_num){
      //접속한 사용자의 id로 user_num값 획득
      int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
      //폴더 생성을 위한 정보를 담을 Directory 객체 생성
      Directory dir = new Directory();
      dir.setDir_name(dir_name);
      dir.setUser_num(user_num);
      dir.setParent_dir_num(parent_dir_num);
      log.debug("dir통과하나요~{}",dir);
      int num = service.dirCreateOne(dir);
   }
   /*
    * html에서 받은 정보를 기반으로 file객체를 생성하는 기능을 가진 메서드
    * 지정된 분류에 따라 추가로 생성하는 객체가 Notepad인지 Word인지 if문으로 구분된다.
    * 
    */
   @ResponseBody
   @PostMapping("fileCreateOne")
   public DirFile fileCreate(@AuthenticationPrincipal UserDetails user
               ,int dir_num, String title, String file_type, String lang_type) {
      //접속한 사용자의 id로 user_num값 획득
      log.debug("디렉토리넘버 파일생성확인용 {}",dir_num);
      int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
      //파일 생성을 위한 정보를 xml까지 가져갈 DirFile 매개변수 생성
      DirFile file = new DirFile();
      log.debug("파일생성{}",user_num);
      //file에 생성을 위한 정보 삽입
      file.setUser_num(user_num);
      file.setDir_num(dir_num);
      file.setTitle(title);
      file.setFile_type(file_type);            //값이 notepad일경우 메모장 생성, word일경우 단어장 본체 생성
      file.setLang_type(lang_type);
      //파일 생성
      log.debug("파일{}",file);
      int num = service.fileCreateOne(file);
      log.debug("num값 {}",num);
      //file_type에 따라 추가생성 정보 분류
      return file;
   }
   //html에서 받은 사진 정보를 기반으로 특정 file객체에 종속되는 사진 객체를 DB에 추가하는 메서드
   @ResponseBody
   @PostMapping("filePhotoCreate")
   public void filePhotoCreate(ArrayList<DirPhoto> photo, @AuthenticationPrincipal UserDetails user) {
	   int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
	   //리턴받은 user_num을 photo 배열 내 모든 사진 객체의 user_num에 집어넣기
	   for(DirPhoto p : photo) {
			p.setUser_num(user_num);
	   }
	   
	   int methodResult = service.filePhotoArrayInsert(photo);
   }

   //html에서 받은 정보를 기반으로 특정 file객체에 종속되는 word객체를 생성하는 기능을 가진 메서드
   @ResponseBody
   @PostMapping("wordCreate")
   public void wordCreate(@AuthenticationPrincipal UserDetails user
               , DirWord inputword) {
      int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
      inputword.setUser_num(user_num);
      
      int num = service.wordCreateOne(inputword);
      if(num != 0) {
    	  int methodResult = service.wordFileUpdate(user_num, inputword.getFile_num());
      }
      log.debug("단어 생성 성공{}", num);
   }
   //-----------[ 노트 생성기능 종료 ]-------------->>>>>>>>>>>>>>
   
   
   
   
   //<<<<<<<<<<<<-----[ 노트 삭제기능 시작 ]-----------------------
   //폴더 번호를 매개변수로 해당 파일을 DB에서 삭제하는 기능
   @ResponseBody
   @PostMapping("dirDeleteOne")
   public String dirDeleteOne(@AuthenticationPrincipal UserDetails user
         , @RequestParam(name="dir_num", defaultValue="-1") int dir_num) {
      if(dir_num == -1) {
         log.debug("파일{}",dir_num);
         return "삭제실패";
      }
      int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
      HashMap<String, Integer> map = new HashMap<>();
      map.put("user_num", user_num);
      map.put("dir_num", dir_num);
      
      int methodResult = service.dirDeleteOne(map);
      if(methodResult==0) {
         return dir_num+"번 파일 삭제실패";
      }
      return dir_num+"번 파일 삭제성공";
   }
   
   //파일번호를 매개변수로 해당 파일을 DB에서 삭제하는 기능
   @ResponseBody
   @PostMapping("fileDeleteOne")
   public String fileDeleteOne(@AuthenticationPrincipal UserDetails user
               , @RequestParam(name="file_num", defaultValue="-1") int file_num) {
      if(file_num == -1) {
         log.debug("파일{}",file_num);
         return "삭제실패";
      }
      int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
      HashMap<String, Integer> map = new HashMap<>();
      map.put("user_num", user_num);
      map.put("file_num", file_num);
      
      int methodResult = service.fileDeleteOne(map);
      if(methodResult==0) {
         return file_num+"번 파일 삭제실패";
      }
      return file_num+"번 파일 삭제성공";
   }
   
   @ResponseBody
   @PostMapping("wordDeleteOne")
   public String wordDeleteOne(@AuthenticationPrincipal UserDetails user, int file_num
		   , @RequestParam(name="word_num", defaultValue="0") int word_num) {
	   int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
	   HashMap<String, Integer> map = new HashMap<>();
	   map.put("user_num", user_num);
       map.put("word_num", word_num);
       log.debug("단어 삭제 맵 키값 {}", map);
	   int deleteResult = service.wordDeleteOne(map);
	   if(deleteResult==0) {
	         return word_num+"번 단어 삭제 실패";
	      }
	   deleteResult = service.wordFileUpdate(user_num, file_num);
	   return word_num + "번 단어 삭제 완료";
   }	
   
   //-----------[ 노트 삭제기능 종료 ]-------------->>>>>>>>>>>>>>
   
   
   

   //<<<<<<<<<<<<-----[ 노트 수정기능 시작 ]-----------------------
   //파일번호, 수정된 파일명(title)을 매개변수로 DB에 전달하여 해당 파일을 수정하는 메서드
   @ResponseBody
   @PostMapping("fileModify")
   public HashMap<String, Object> fileModify(int file_num, String title
            , @AuthenticationPrincipal UserDetails user) {
      int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
      HashMap<String, Object> map = new HashMap<>();
      map.put("user_num", user_num);
      map.put("file_num", file_num);
      map.put("title", title);
      log.debug("파일명 수정: {}", map);
      int methodResult = service.fileUpdateOne(map);

      return map;
   }
   //메모장 작성 완료 후 수정한 작성파일을 DB로 보내는 메서드
   @ResponseBody
   @PostMapping("fileTextModifie")
   public String fileSave(int file_num, String file_text
            , @AuthenticationPrincipal UserDetails user) {
      int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
      HashMap<String, Object> map = new HashMap<>();
      map.put("user_num", user_num);
      map.put("file_num", file_num);
      map.put("file_text", file_text);
      int methodResult = service.fileTextUpdateOne(map);
      if(methodResult ==0) {
    	  return "수정 실패";
      }
      return "수정성공";
   }

   //단어 수정 전 수정할 단어 객체를 호출하는 메서드
   @ResponseBody
   @PostMapping("wordModifyPrint")
   public DirWord wordModifyPrint(int word_num
         , @AuthenticationPrincipal UserDetails user) {
      int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
      HashMap<String, Integer> map = new HashMap<>();
      map.put("word_num", word_num);
      map.put("user_num", user_num);
      DirWord word = service.wordSearchByWordNum(map);
      return word;
   }
   //수정 이벤트 중 좌우 화살표를 클릭해서 해당 파일의 직전/직후 단어 수정으로 이동하는 메서드
   @ResponseBody
   @PostMapping("wordMoveWidth")
   public DirWord wordMoveWidth(int word_num, int file_num, String arrow) {
	   log.debug("옆 단어 이동 파라미터들: {} {} {}", word_num, file_num, arrow);
	   HashMap<String, Object> map = new HashMap<>();
	   HashMap<String, Object> checkMap = new HashMap<>();
	   
	   checkMap.put("arrow", arrow);
	   checkMap.put("file_num", file_num);
	   
	   int lastWord = service.checkLastWord(checkMap);
	   
	   if(arrow.equals("right") && word_num <= lastWord) {
		   DirWord endWord = new DirWord();
		   endWord.setIsEndOfWords(0);
		   return endWord;
	   } 
	   else if(arrow.equals("left") && word_num >= lastWord) { 
		   DirWord firstWord = new DirWord();
		   firstWord.setIsEndOfWords(1);
		   return firstWord; 
	   }

	   map.put("word_num", word_num);
	   map.put("file_num", file_num);
	   map.put("arrow", arrow);
	   DirWord word = service.wordSearchByArrow(map);
	   log.debug("옆단어로 이동 : {} 맵값 {}", word, map);
	   return word;
   }
   //수정한 단어 객체를 DB에 전달해 수정하는 메서드
   @ResponseBody
   @PostMapping("wordModify")
   public String wordModify(DirWord word
            , @AuthenticationPrincipal UserDetails user) {
      int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
      log.debug("수정 내용 출력 : {}", word);
      word.setUser_num(user_num);
      int methodResult = service.wordUpdateOne(word);
      methodResult = service.wordFileUpdate(user_num, word.getFile_num());
      return "수정성공";
   }
   
   //-----------[ 노트 수정기능 종료 ]-------------->>>>>>>>>>>>>>
   @GetMapping("noteWord")
   public String noteWord() {
      return "note/noteWord";
   }
}