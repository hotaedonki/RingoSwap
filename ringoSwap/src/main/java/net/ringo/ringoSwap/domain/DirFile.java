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
	int size;
	String inputdate;
	String modifie_date;
	String file_type;
}
