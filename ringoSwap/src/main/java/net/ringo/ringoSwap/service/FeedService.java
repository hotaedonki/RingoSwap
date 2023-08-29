package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import net.ringo.ringoSwap.domain.Feed;
import net.ringo.ringoSwap.domain.FeedPhoto;
import net.ringo.ringoSwap.domain.Reply;

public interface FeedService {

	//<<<<<<<<<<<------[피드 출력 기능 시작]----------------------
	//모든 게시물을 최신순으로 리턴하는 메서드
	public ArrayList<Feed> feedSelectAll();
	//최근 게시물들을 인기순(좋아요 순)으로 리턴하는 메서드
	public ArrayList<Feed> feedSelectPopularAll();
	//feed_num을 매개변수로 특정 피드 게시글 정보를 리턴하는 메서드
	public Feed feedSelectOneByFeedNum(int feed_num);
	//feed_num을 매개변수로 특정 댓글 배열 정보를 리턴하는 메서드
	public ArrayList<Reply> replySelectByFeedNum(int feed_num);
	//feed_num을 매개변수로 해당 피드에 등록된 사진 정보 배열 객체를 불러오는 메서드
	public ArrayList<FeedPhoto> feedPhotoSelectByFeedNum(int feed_num);
	//----------------[피드 출력 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[피드 작성 기능 시작]----------------------
	//작성한 Reply 객체를 DB에 매개변수로 전달하는 메서드
	public int replyInsert(Reply reply);
}
