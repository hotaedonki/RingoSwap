package net.ringo.ringoSwap.dao;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.Directory;


@Mapper
public interface NoteDAO 
{
	//사용자의 id를 xml에 매개변수로 넘겨, id로 검색하여 나온 폴더 정보를 배열로 리턴하는 메서드
	ArrayList<Directory> selectUserDirectoryAll(int user_num);
	//사용자의 id를 xml에 매개변수로 넘겨, id로 검색하여 나온 파일 정보를 배열로 리턴하는 메서드
	ArrayList<DirFile> selectUserFileAll(HashMap<String, Object> map);
	
}