package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberFollow {
	int follower_num;			//자신을 팔로잉한 팔로워의 회원번호
	int followee_num;			//자신이 팔로우한 팔로위의 회원번호
	String follower_id;			//tagstorage에서 tag값을 담는 변수 / 미리 지정된 20개 전후의 변수가 담겨져 있고 유저는 수정/추가/삭제 불가.
	String followee_id;
	String original_profile;
	String saved_profile;
	public String toString()
	{
		return String.format("follower_num : {%d} / followee_num : {%d} / follower_id : {%s} / followee_id {%s} / original_profile {%s} / saved_profile {%s}"
				,follower_num ,followee_num ,follower_id, followee_id, original_profile, saved_profile);
	}
}
