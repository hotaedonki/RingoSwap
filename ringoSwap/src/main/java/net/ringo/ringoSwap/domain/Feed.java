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
	String user_id;
	String original_profile;
	String saved_profile;	
	int like_num;								//각 피드당 좋아요 수를 기록하기위한 추가 변수
	String tag_list;							//피드 작성시 해당 피드에 추가한 태그 전체를 문자열로 저장하는 변수
	String[] tagList;
	public String toString()
	{
		return String.format("feed_num : {%d} / user_num : {%d} / user_id : {%s} / contents : {%s} / inputdate : {%s} / like_num : {%d} / tag_list : {%s}"
				, feed_num, user_num, user_id, contents, inputdate, like_num, tag_list);
	}
}
