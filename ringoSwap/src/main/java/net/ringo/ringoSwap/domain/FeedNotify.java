package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedNotify {
	int feed_num;
	int feed_user;
	int notify_user;
	String nickname;
	String reply_content;
	String notify_type;
	String inputdate;
	
	public String toString()
	{
		return String.format("feed_num : {%d} / feed_user : {%d} / notify_user : {%d} / nickname : {%s} / reply_content : {%s} / notify_type : {%s} / inputdate : {%s}"
				, feed_num, feed_user, notify_user, nickname, reply_content, notify_type, inputdate);
	}
}
