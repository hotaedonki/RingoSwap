package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirNotepad;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.Directory;

public interface NoteService {
	//사용자가 작성한 폴더를 전부 검색하여 폴더목록을 리턴하는 메서드
	ArrayList<Directory> selectUserDirectoryAll(int user_num);
	//사용자가 작성한 파일을 전부 검색하여 파일목록을 리턴하는 메서드
	ArrayList<DirFile> selectUserFileAll(HashMap<String, Object> map);
	//매개변수로 주어지는 부모폴더 정보로 검색된 폴더목록을 리턴하는 메서드
	ArrayList<Directory> selectDirectoryByPDirNum(int dir_num);
	//매개변수로 주어지는 부모폴더 정보로 검색된 파일목록을 리턴하는 메서드
	ArrayList<DirFile> selectFileByDirNum(HashMap<String, Object> map);
	//file_num을 매개변수로 검색해 나온 file객체에서 해당 파일의 title값만 리턴하는 메서드
	String selectFileByFileNumReturnTitle(int file_num);
	//file_num을 매개변수로 해당 파일에 속하는 notepad 객체를 리턴하는 메서드
	DirNotepad selectNotepadByFileNum(int file_num);
	//file_num을 매개변수로 해당 파일에 속하는 word객체 목록을 리턴하는 메서드 
	ArrayList<DirWord> selectWordArrayByFileNum(int file_num);

}
