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
	String follower_name;
	String followee_name;
	String follower_id;
	String followee_id;
	String original_profile;
	String saved_profile;
	public String toString()
	{

		return String.format("follower_num : {%d} / followee_num : {%d} / follower_name : {%s} / followee_name {%s} / original_profile {%s} / saved_profile {%s}"
				,follower_num ,followee_num ,follower_name, followee_name, original_profile, saved_profile);
	}
}
