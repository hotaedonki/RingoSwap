package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.Directory;

public interface NoteService {
	//사용자가 작성한 폴더를 전부 검색하여 폴더목록을 리턴하는 메서드
	ArrayList<Directory> selectUserDirectoryAll(int user_num);
	//사용자가 작성한 파일을 전부 검색하여 파일목록을 리턴하는 메서드
	ArrayList<DirFile> selectUserFileAll(HashMap<String, Object> map);

}
