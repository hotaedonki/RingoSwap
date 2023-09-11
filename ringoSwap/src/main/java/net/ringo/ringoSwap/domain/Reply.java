package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
	int reply_num;
	int user_num;
	int feed_num;
	int parent_reply_num;
	String contents;
	String inputdate;
	String user_id;	
	String username;
	int like_num;								//각 피드당 좋아요 수를 기록하기위한 추가 변수
	String tag_list;							//피드 작성시 해당 피드에 추가한 태그 전체를 문자열로 저장하는 변수
	String[] tagList;
	
	public String toString()
	{
		return String.format("리플라이 번호 {%d} / 유저 번호 {%d} / 피드 번호 {%d} / 내용 {%s} / 유저 아이디 {%s} /닉네임 {%s} / 날짜 {%s} / 좋아요 개수 {%d} / 태그리스트 {%s}"
				, reply_num, user_num, feed_num, contents, user_id, username, inputdate, like_num, tag_list);
	}
}
