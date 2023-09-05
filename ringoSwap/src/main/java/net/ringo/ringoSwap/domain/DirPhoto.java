package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirPhoto {
	int file_num;
	int photo_num;
	int user_num;
	String inputdate;
	String original_note;
	String saved_note;
	
	public String toStirng()
	{
		return String.format("file_num : {%d} / photo_num : {%d} / user_num : {%d} / inputdate : {%s} / original_note : {%s} / saved_note : {%d}"
				, file_num, photo_num, user_num, inputdate, original_note, saved_note);
	}
}
