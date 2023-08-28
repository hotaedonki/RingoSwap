package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evernote.edam.notestore.NoteCollectionCounts;
import com.evernote.edam.notestore.NoteFilter;

import net.ringo.ringoSwap.dao.NoteDAO;
import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirNotepad;
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

	//file_num을 매개변수로 해당 파일에 속하는 notepad 객체를 리턴하는 메서드
	@Override
	public DirNotepad selectNotepadByFileNum(int file_num) {
		return dao.selectNotepadByFileNum(file_num);
	}
	//file_num을 매개변수로 해당 파일에 속하는 word객체 목록을 리턴하는 메서드 
	@Override
	public ArrayList<DirWord> selectWordArrayByFileNum(int file_num){
		return dao.selectWordArrayByFileNum(file_num);
	}
}
