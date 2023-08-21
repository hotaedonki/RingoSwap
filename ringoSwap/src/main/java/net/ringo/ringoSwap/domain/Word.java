package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {
	int file_num;
	int dir_num;
	int user_num;
	String word;
	String pron;
	String mean;
	String inputdate;
}
