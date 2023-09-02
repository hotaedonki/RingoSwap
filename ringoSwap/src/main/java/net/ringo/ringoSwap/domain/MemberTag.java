package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberTag {
	int user_num;			//taglink_member에서 user_num값을 담는 변수
	int tag_num;				//taglink_member와 tagstorage에서 tag_num값을 담는 변수
	String tag;					//tagstorage에서 tag값을 담는 변수 / 미리 지정된 20개 전후의 변수가 담겨져 있고 유저는 수정/추가/삭제 불가.
}
