package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notepad {
	int file_num;
	int dir_num;
	int user_num;
	String text;
	String inputdate;
	String modifie_date;
}
