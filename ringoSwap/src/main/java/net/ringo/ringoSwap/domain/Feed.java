package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feed {
	int feed_num;
	int user_num;
	String contents;
	String inputdate;
	int like_num;								//각 피드당 좋아요 수를 기록하기위한 추가 변수
}
