package net.ringo.ringoSwap.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.Feed;
import net.ringo.ringoSwap.domain.FeedPhoto;
import net.ringo.ringoSwap.domain.Reply;
import net.ringo.ringoSwap.domain.Tagstorage;


@Mapper
public interface FeedDAO 
{

	//<<<<<<<<<<<------[피드 출력 기능 시작]----------------------
	//주어진 매개변수에 의해 검색된 게시물 목록을 리턴하는 메서드
	//정렬순서 변수에 따라 최신순/좋아요 순으로 출력하며, 닉네임/피드글/태그에 검색 문자열의 값이 포함된 피드의 목록을 검색해 출력한다.
	ArrayList<Feed> feedSelectAll(HashMap<String, Object> map);
	//지정된 feed_num 배열을 매개변수로 주고, 그에 따른 각 feed의 사진 배열을 리턴하는 메서드
	ArrayList<FeedPhoto> feedPhotoSelectArrayByFeedNum(ArrayList<Integer> feed_num);
	//feed_num을 매개변수로 특정 feed 게시글 정보를 리턴하는 메서드
	Feed feedSelectOneByFeedNum(int feed_num);
	//feed_num을 매개변수로 특정 reply 배열 정보를 리턴하는 메서드
	ArrayList<Reply> replySelectByFeedNum(int feed_num);
	//feed_num을 매개변수로 해당 피드에 등록된 사진 정보 배열 객체를 불러오는 메서드
	ArrayList<FeedPhoto> feedPhotoSelectByFeedNum(int feed_num);
	
	//----------------[피드 출력 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[피드 작성 기능 시작]----------------------
	//작성한 feed 객체를 DB에 매개변수로 전달하는 메서드
	int feedInsert(Feed feed);
	//작성한 feedPhoto 배열을 DB에 매개변수로 전달하는 메서드
	int feedPhotoInsert(FeedPhoto photo);
	//작성한 Reply 객체를 DB에 매개변수로 전달하는 메서드
	int replyInsert(Reply reply);
	//----------------[피드 작성 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[피드&댓글 좋아요 기능 시작]----------------------
	//사용자가 해당 피드에 좋아요를 줬는지 여부를 확인해 리턴하는 메서드
	int feedLikeSelectOneForCheck(HashMap<String, Integer> map);
	//해당 피드에 사용자의 좋아요를 insert하는 메서드
	int feedLikeInsertOne(HashMap<String, Integer> map);
	//해당 피드에서 사용자의 좋아요를 delete하는 메서드
	int feedLikeDeleteOne(HashMap<String, Integer> map);
	//특정 피드의 현재 좋아요 갯수를 리턴하는 메서드
	int feedLikeCountSelectByFeedNum(int feed_num);
	//사용자가 해당 댓글에 좋아요를 줬는지 여부를 확인해 리턴하는 메서드
	int replyLikeSelectOneForCheck(HashMap<String, Integer> map);
	//해당 댓글에 사용자의 좋아요를 insert하는 메서드
	int replyLikeInsertOne(HashMap<String, Integer> map);
	//해당 댓글에서 사용자의 좋아요를 delete하는 메서드
	int replyLikeDeleteOne(HashMap<String, Integer> map);
	//특정 댓글의 현재 좋아요 갯수를 리턴하는 메서드
	int replyLikeCountSelectByFeedNum(int reply_num);
	//----------------[피드&댓글 좋아요 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[태그 관련 기능 시작]----------------------
	//피드에 달린 태그 중 하나를 클릭할경우, 해당 태그가 달린 피드와 태그가 달린 댓글이 달린 피드를 검색해서 리턴하는 메서드
	ArrayList<Feed> feedArraySearchByTagName(Map<String, String> tagMap);
	//----------------[피드&댓글 좋아요 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[태그 관련 기능 시작]----------------------
	int insertHashtag(String hashtag);
	//해당하는 태그명으로 태그를 DB에 삽입하는 메서드
	int getTagNumByTagName(String hashtag);
	//태그 링크
	int linkHashtagToFeed(Map<String, Object> hashtagLinkMap);
	//----------------[태그 관련 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[삭제 관련 기능 시작]----------------------
	//해당 피드작성자인지를 확인한 후 해당 피드를 삭제하는 메서드
	int feedDeleteByUser(HashMap<String, Object> map);
	//해당 댓글의 작성자인지 확인한 후 해당 댓글을 삭제하는 메서드
	int replyDeleteOne(HashMap<String, Object> map);
	//----------------[삭제 관련 기능 종료]----------->>>>>>>>>>>>
}