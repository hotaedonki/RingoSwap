package net.ringo.ringoSwap.dao;


import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.SingleDifficulty;


@Mapper
public interface GameDAO 
{
	/*
	 * user_id를 매개변수로 가져가, 해당 사용자의 싱글게임 난이도 설정 정보를 select로 검색하여
	 * 난이도 정보를 리턴값으로 받아오는 메서드.
	 */
	SingleDifficulty difficultyCall(String user_id);

	//user_num을 매개변수로 file_type이 word인 파일목록을 리턴하는 메서드
	ArrayList<DirFile> wordFileSelectByUserNum(int user_num);
	
}