package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.Directory;
import net.ringo.ringoSwap.util.PageNavigator;

public interface NoteService {
	//<<<<<<<<<<<<-----[ 노트 출력기능 시작 ]-----------------------
	//사용자가 작성한 폴더를 전부 검색하여 폴더목록을 리턴하는 메서드
	ArrayList<Directory> selectUserDirectoryAll(int user_num);
	//사용자가 작성한 파일을 전부 검색하여 파일목록을 리턴하는 메서드
	ArrayList<DirFile> selectUserFileAll(HashMap<String, Object> map);
	//매개변수로 주어지는 부모폴더 정보로 검색된 폴더목록을 리턴하는 메서드
	ArrayList<Directory> selectDirectoryByPDirNum(int dir_num);
	//매개변수로 주어지는 부모폴더 정보로 검색된 파일목록을 리턴하는 메서드
	ArrayList<DirFile> selectFileByDirNum(HashMap<String, Object> map);
	//매개변수로 file_num을 받아 해당 번호의 파일 객체를 리턴하는 메서드
	public DirFile selectFileByFileNum(int file_num);
	//file_num을 매개변수로 검색해 나온 file객체에서 해당 파일의 title값만 리턴하는 메서드
	String selectFileByFileNumReturnTitle(int file_num);
	
	//file_num을 매개변수로 해당 파일에 속하는 단어목록 네비게이터를 생성하는 메서드
	PageNavigator wordSelectPageNavigator(int pagePerGroup, int countPerPage, int page, int file_num);
	//file_num을 매개변수로 해당 파일에 속하는 word객체 목록을 리턴하는 메서드 
	ArrayList<DirWord> selectWordArrayByFileNum(PageNavigator navi, int file_num);
	//-----------[ 노트 출력기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 생성기능 시작 ]-----------------------
	//폴더를 생성하는 메서드
	int dirCreateOne(Directory dir);
	//파일을 생성하는 메서드
	int fileCreateOne(DirFile file);
	//파일 부속 객체인 Word를 생성하는 메서드
	int wordCreateOne(DirWord word);
	//-----------[ 노트 생성기능 종료 ]-------------->>>>>>>>>>>>>>
	
	//<<<<<<<<<<<<-----[ 노트 삭제기능 시작 ]-----------------------
	//dir_num을 매개변수로 해당 폴더를 DB에서 삭제하는 기능
	int dirDeleteOne(HashMap<String, Integer> map);
	//파일번호를 매개변수로 해당 파일을 DB에서 삭제하는 기능
	int fileDeleteOne(HashMap<String, Integer> map);
	//-----------[ 노트 삭제기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 수정기능 시작 ]-----------------------
	//수정된 파일번호를 보내 해당 파일번호의 파일의 이름을 수정하는 기능
	int fileUpdateOne(HashMap<String, Object> map);
	//메모장 작성 완료 후 수정한 작성파일을 DB로 보내는 메서드
	int fileTextUpdateOne(HashMap<String, Object> map);
	//단어 수정전 수정할 단어를 DB에서 검색해 리턴하는 메서드
	DirWord wordSearchByWordNum(HashMap<String, Integer> map);
	//수정한 단어 객체를 DB에 전달해 수정하는 메서드
	int wordUpdateOne(DirWord word);
	// word_num을 받아와서 해당 단어 삭제
	int wordDeleteOne(HashMap<String, Integer> map);
}
