package net.ringo.ringoSwap.dao;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.Directory;


@Mapper
public interface NoteDAO 
{
	//사용자의 id를 xml에 매개변수로 넘겨, id로 검색하여 나온 폴더 정보를 배열로 리턴하는 메서드
	ArrayList<Directory> selectUserDirectoryAll(int user_num);
	//사용자의 id를 xml에 매개변수로 넘겨, id로 검색하여 나온 파일 정보를 배열로 리턴하는 메서드
	ArrayList<DirFile> selectUserFileAll(HashMap<String, Object> map);
	//폴더번호를 매개변수로 해당 폴더에 속한 하위 폴더 배열을 리턴하는 메서드
	ArrayList<Directory> selectDirectoryByPDirNum(int dir_num);
	//폴더 번호를 매개변수로 해당 폴더에 속한 파일 배열을 리턴하는 메서드
	ArrayList<DirFile> selectFileByDirNum(HashMap<String, Object> map);
	//file_num을 매개변수로 DB에서 검색해 출력된 file객체를 리턴하는 메서드
	DirFile selectFileByFileNum(int file_num);
	//file_num을 매개변수로 해당 파일에 속한 단어 갯수를 리턴하는 메서드
	int wordSelectByFileNum(int file_num);
	//file_num을 매개변수로 DB에서 검색해 출력된 word객체 목록을 리턴하는 메서드
	ArrayList<DirWord> selectWordArrayByFileNum(int file_num, RowBounds rb);
	//-----------[ 노트 출력기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 생성기능 시작 ]-----------------------
	//폴더를 생성하는 메서드
	int dirCreate(Directory dir);
	//파일을 생성하는 메서드
	int fileCreateOne(DirFile file);
	//파일 부속 객체인 notepad를 생성하는 메서드 
	//파일 부속 객체인 Word를 생성하는 메서드
	int wordCreateOne(DirWord word);

	//-----------[ 노트 생성기능 종료 ]-------------->>>>>>>>>>>>>>
	
	//<<<<<<<<<<<<-----[ 노트 삭제기능 시작 ]-----------------------
	//dir_num을 매개변수로 해당 폴더를 DB에서 삭제한 후, 삭제여부를 리턴받는 메서드
	int dirDeleteOne(HashMap<String, Integer> map);
	//file_num을 매개변수로 해당 파일을 DB에서 삭제한 후, 삭제여부를 리턴받는 메서드
	int fileDeleteOne(HashMap<String, Integer> map);
	//-----------[ 노트 삭제기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 수정기능 시작 ]-----------------------
	//수정된 파일번호를 보내 해당 파일번호의 파일의 이름을 수정하는 기능
	int fileUpdateOne(HashMap<String, Object> map);
	//메모장 작성 완료 후 수정한 작성파일을 DB로 보내 수정하는 메서드
	int fileTextUpdateOne(HashMap<String, Object> map);
	//단어 수정전 수정할 단어 객체를 DB에서 검색해서 리턴하는 메서드
	DirWord wordSearchByWordNum(HashMap<String, Integer> map);
	//수정한 단어 객체를 DB에 전달해 수정하는 메서드
	int wordUpdateOne(DirWord word);
}