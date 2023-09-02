package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ringo.ringoSwap.dao.NoteDAO;
import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.Directory;

@Service
public class NoteServiceImple implements NoteService{
	@Autowired
	NoteDAO dao;
	
	//사용자의 아이디를 매개변수로 dao파일에 넘겨, dao로부터 해당 id로 검색하여 나온 모든 폴더 정보를 배열로 리턴받는 메서드
	@Override
	public ArrayList<Directory> selectUserDirectoryAll(int user_num){
		return dao.selectUserDirectoryAll(user_num);
	}
	//사용자의 아이디를 매개변수로 dao파일에 넘겨, dao로부터 해당 id로 검색하여 나온 모든 파일 정보를 배열로 리턴받는 메서드
	@Override
	public ArrayList<DirFile> selectUserFileAll(HashMap<String, Object> map){
		return dao.selectUserFileAll(map);
	}

	//매개변수로 주어지는 부모폴더 정보로 검색된 폴더목록을 리턴하는 메서드
	@Override
	public ArrayList<Directory> selectDirectoryByPDirNum(int dir_num){
		return dao.selectDirectoryByPDirNum(dir_num);
	}
	//매개변수로 주어지는 부모폴더 정보로 검색된 파일목록을 리턴하는 메서드
	@Override
	public ArrayList<DirFile> selectFileByDirNum(HashMap<String, Object> map){
		return dao.selectFileByDirNum(map);
	}
	
	//매개변수로 file_num을 받아 해당 번호의 파일 객체를 리턴하는 메서드
	@Override
	public DirFile selectFileByFileNum(int file_num) {
		return dao.selectFileByFileNum(file_num);
	}
	
	//file_num을 매개변수로 검색해 나온 file객체에서 해당 파일의 title값만 리턴하는 메서드
	@Override
	public String selectFileByFileNumReturnTitle(int file_num) {
		DirFile file = dao.selectFileByFileNum(file_num);
		String title = file.getTitle();
		return title;
	}

	//file_num을 매개변수로 해당 파일에 속하는 word객체 목록을 리턴하는 메서드 
	@Override
	public ArrayList<DirWord> selectWordArrayByFileNum(int file_num){
		return dao.selectWordArrayByFileNum(file_num);
	}
	//-----------[ 노트 출력기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 생성기능 시작 ]-----------------------
	//폴더를 생성하는 메서드
	@Override
	public int dirCreateOne(Directory dir) {
		return dao.dirCreate(dir);
	}
	//파일을 생성하는 메서드
	@Override
	public int fileCreateOne(DirFile file) {
		return dao.fileCreateOne(file);
	}
	
	//파일 부속 객체인 Word를 생성하는 메서드
	@Override
	public int wordCreateOne(DirWord word) {
		return dao.wordCreateOne(word);
	}
	//-----------[ 노트 생성기능 종료 ]-------------->>>>>>>>>>>>>>
	
	//<<<<<<<<<<<<-----[ 노트 삭제기능 시작 ]-----------------------
	//dir_num을 매개변수로 해당 폴더를 DB에서 삭제하는 기능
	@Override
	public int dirDeleteOne(HashMap<String, Integer> map) {
		return dao.dirDeleteOne(map);
	}
	
	//파일번호를 매개변수로 해당 파일을 DB에서 삭제하는 기능
	@Override
	public int fileDeleteOne(HashMap<String, Integer> map) {
		return dao.fileDeleteOne(map);
	}
	//-----------[ 노트 삭제기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 수정기능 시작 ]-----------------------
	//수정된 파일번호를 보내 해당 파일번호의 파일의 이름을 수정하는 기능
	@Override
	public int fileUpdateOne(HashMap<String, Object> map) {
		return dao.fileUpdateOne(map);
	}
}
