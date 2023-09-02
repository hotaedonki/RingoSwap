package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirFile {
	int file_num;
	int dir_num;
	int user_num;
	String title;
	String file_text;
	int size;
	String inputdate;
	String modifie_date;
	String lang_type;
	String file_type;
	
	public String toStirng()
	{
		return String.format("file_num : {%d} / dir_num : {%d} / user_num : {%d} / title : {%s} / file_text : {%s} / size : {%d} / inputdate : {%s} / modifie_date : {%s} / lang_type : {%s} / file_type : {%s}"
				, file_num, dir_num, user_num, title, file_text, file_text, size, inputdate, modifie_date, lang_type, file_type);
	}
}
