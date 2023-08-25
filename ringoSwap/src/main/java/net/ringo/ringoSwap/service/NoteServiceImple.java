package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ringo.ringoSwap.dao.NoteDAO;
import net.ringo.ringoSwap.domain.DirFile;
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
	public ArrayList<DirFile> selectUserFileAll(int user_num){
		return dao.selectUserFileAll(user_num);
	}

}
