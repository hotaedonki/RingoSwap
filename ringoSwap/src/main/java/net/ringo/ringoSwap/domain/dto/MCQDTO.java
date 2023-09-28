package net.ringo.ringoSwap.domain.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ringo.ringoSwap.domain.DirWord;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MCQDTO {
	private ArrayList<DirWord> wordList;
	private int index;
	private String correctAnswer;
	private String formType;
}
