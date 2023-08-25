package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirNotepad {
	//메모장 분류의 DirFile에 기입되는 텍스트 정보를 저장하는 ringo_notepad테이블과 관련있는 VO객체
	int file_num;
	int dir_num;
	int user_num;
	String text;
	String inputdate;
	String modifie_date;
}
