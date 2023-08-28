package net.ringo.ringoSwap.dao;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirNotepad;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.Directory;


@Mapper
public interface NoteDAO 
{
	//사용자의 id를 xml에 매개변수로 넘겨, id로 검색하여 나온 폴더 정보를 배열로 리턴하는 메서드
	ArrayList<Directory> selectUserDirectoryAll(int user_num);
	//사용자의 id를 xml에 매개변수로 넘겨, id로 검색하여 나온 파일 정보를 배열로 리턴하는 메서드
	ArrayList<DirFile> selectUserFileAll(HashMap<String, Object> map);
	//
	ArrayList<Directory> selectDirectoryByPDirNum(int dir_num);
	//
	ArrayList<DirFile> selectFileByDirNum(HashMap<String, Object> map);
	//file_num을 매개변수로 DB에서 검색해 출력된 file객체를 리턴하는 메서드
	DirFile selectFileByFileNum(int file_num);
	//file_num을 매개변수로 DB에서 검색해 출력된 notepad객체를 리턴하는 메서드
	DirNotepad selectNotepadByFileNum(int file_num);
	//file_num을 매개변수로 DB에서 검색해 출력된 word객체 목록을 리턴하는 메서드
	ArrayList<DirWord> selectWordArrayByFileNum(int file_num);
	//-----------[ 노트 출력기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 생성기능 시작 ]-----------------------
	//폴더를 생성하는 메서드
	int dirCreate(Directory dir);
	//파일을 생성하는 메서드
	int fileCreateOne(DirFile file);
	//파일 부속 객체인 notepad를 생성하는 메서드
	int notepadCreateOne(DirNotepad note);
	//파일 부속 객체인 Word를 생성하는 메서드
	int wordCreateOne(DirWord word);
	
}