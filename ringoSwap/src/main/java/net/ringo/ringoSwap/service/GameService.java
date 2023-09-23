package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.SingleDifficulty;

public interface GameService {

	public SingleDifficulty difficultyCall(String user_id);

	//user_num을 매개변수로 file_type이 word인 파일목록을 리턴하는 메서드
	public ArrayList<DirFile> wordFileSelectByUserNum(int user_num);

}
