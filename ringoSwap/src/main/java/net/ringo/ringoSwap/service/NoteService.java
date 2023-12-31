package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirPhoto;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.Directory;
import net.ringo.ringoSwap.util.PageNavigator;

public interface NoteService {
	//<<<<<<<<<<<<-----[ 노트 출력기능 시작 ]-----------------------
	//사용자가 작성한 폴더를 전부 검색하여 폴더목록을 리턴하는 메서드
	ArrayList<Directory> userDirectorySelectAll(int user_num);
	//사용자가 작성한 파일을 전부 검색하여 파일목록을 리턴하는 메서드
	ArrayList<DirFile> userFileSelectAll(HashMap<String, Object> map);
	//매개변수로 주어지는 부모폴더 정보로 검색된 폴더목록을 리턴하는 메서드
	ArrayList<Directory> directorySelectByPDirNum(int dir_num);
	//매개변수로 주어지는 부모폴더 정보로 검색된 파일목록을 리턴하는 메서드
	ArrayList<DirFile> fileSelectByDirNum(HashMap<String, Object> map);
	//매개변수로 file_num을 받아 해당 번호의 파일 객체를 리턴하는 메서드
	public DirFile fileSelectByFileNum(int file_num);
	//매개변수로 file_num을 받아 해당 파일에 기록된 사진 목록을 배열로 리턴하는 메서드
	ArrayList<DirPhoto> filePhotoSelectByFileNum(int file_num);
	//file_num을 매개변수로 검색해 나온 file객체에서 해당 파일의 title값만 리턴하는 메서드
	String fileSelectByFileNumReturnTitle(int file_num);
	
	//file_num을 매개변수로 해당 파일에 속하는 단어목록 네비게이터를 생성하는 메서드
	PageNavigator wordSelectPageNavigator(int pagePerGroup, int countPerPage, int page, int file_num);
	//file_num을 매개변수로 해당 파일에 속하는 word객체 목록을 리턴하는 메서드 
	ArrayList<DirWord> selectWordArrayByFileNum(PageNavigator navi, int file_num);
	//사용자의 가장 최근 수정한 파일을 출력하는 메서드
	DirFile fileSelectByModifieDate(int user_num);
	//-----------[ 노트 출력기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 생성기능 시작 ]-----------------------
	//폴더를 생성하는 메서드
	int dirCreateOne(Directory dir);
	//파일을 생성하는 메서드
	int fileCreateOne(DirFile file);
	//특정 파일에 속한 사진 객체 배열을 생성하는 메서드
	int filePhotoArrayInsert(ArrayList<DirPhoto> photo);
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
	//화살표 클릭시 해당 단어를 기준으로 단어장 내 직전/직후 단어를 리턴하는 메서드
	DirWord wordSearchByArrow(HashMap<String, Object> map);
	// word_num을 받아와서 해당 단어 삭제
	int wordDeleteOne(HashMap<String, Integer> map);
	// 마지막 단어인지 체크
	int checkLastWord(HashMap<String, Object> checkMap);
	//단어장에서 단어를 추가/수정/삭제할때마다 단어장의 수정일자 정보를 현재 시각으로 수정하는 메서드
	int wordFileUpdate(int user_num, int file_num);
}
