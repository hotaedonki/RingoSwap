package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedTag {
	int feed_num;				//taglink_feed에서 사용할때 feed_num값을 담는 변수
	int reply_num;				//taglink_reply에서 사용할때 reply_num값을 담는 변수
	int tag_num;					//taglink와 tagstorage에서 사용할때 tag_num값을 담는 변수
	int link_num;					//taglink에서 해당 사용자가 피드/댓글 생성시 특정 태그를 몇번째로 태그 걸었는지 그 순번을 저장하는 변수
	String tag_name;			//feed 서비스에서 사용하는 tag정보를 담는 변수
	boolean tag_bann;		//해당 태그가 금지어로 설정되었는지 여부에 대한 정보를 담는 변수, true=금지어 설정, false=금지어 미설정
}
