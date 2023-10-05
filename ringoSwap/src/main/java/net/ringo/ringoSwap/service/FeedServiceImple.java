package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.dao.FeedDAO;
import net.ringo.ringoSwap.domain.Feed;
import net.ringo.ringoSwap.domain.FeedNotify;
import net.ringo.ringoSwap.domain.FeedPhoto;
import net.ringo.ringoSwap.domain.Member;
import net.ringo.ringoSwap.domain.Reply;
import net.ringo.ringoSwap.domain.Tagstorage;
import net.ringo.ringoSwap.util.JsonRead;
import net.ringo.ringoSwap.util.PageNavigator;
@Slf4j
@Service
public class FeedServiceImple implements FeedService{
	@Autowired
	FeedDAO dao;

	//<<<<<<<<<<<------[피드 출력 기능 시작]----------------------
	//모든 게시물을 최신순으로 리턴하는 메서드
	@Override
	public ArrayList<Feed> feedSelectAllWithFeedArrayType(String feedArrayType, String text, int offset, int limit, String user_id){
		HashMap<String, Object> map = new HashMap<>();	//매개변수 전달용 해시맵 변수
		ArrayList<Feed> feedList = new ArrayList<>();			//피드목록 출력용 배열 변수
		map.put("feedArrayType", feedArrayType);
		map.put("text", text);
		map.put("offset", offset);
		map.put("limit", limit);
		map.put("user_id", user_id);
	    log.debug("확인?{}", map);
		feedList = dao.feedSelectAll(map);	//피드타입, 검색하는 문자열을 매개변수로 전달해 그에 따른 게시글 배열을 리턴하는 메서드 실행
	    log.debug("확인완료 {}", feedList);
	
		return feedList;
	}
	//feed_num을 매개변수로 특정 feed 게시글 정보를 리턴하는 메서드
	@Override
	public Feed feedSelectOneByFeedNum(int feed_num) {
		return dao.feedSelectOneByFeedNum(feed_num);
	}
	//feed_num을 매개변수로 특정 reply 배열 정보를 리턴하는 메서드
	@Override
	public ArrayList<Reply> replySelectByFeedNum(int feed_num){
		return dao.replySelectByFeedNum(feed_num);
	}
	
	//feed_num을 매개변수로 해당 피드에 등록된 사진 정보 배열 객체를 불러오는 메서드
	@Override
	public ArrayList<FeedPhoto> feedPhotoSelectByFeedNum(int feed_num){
		return dao.feedPhotoSelectByFeedNum(feed_num);
	}
	//----------------[피드 출력 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[피드 작성 기능 시작]----------------------
	//작성한 feed 객체를 DB에 매개변수로 전달하는 메서드
	@Override
	public int feedInsert(Feed feed) {
		return dao.feedInsert(feed);
	}
	//작성한 feedPhoto 배열을 DB에 매개변수로 전달하는 메서드
	@Override
	public int feedPhotoInsert(FeedPhoto photo) {
		return dao.feedPhotoInsert(photo);
	}
	
	//작성한 Reply 객체를 DB에 매개변수로 전달하는 메서드
	@Override
	public int replyInsert(Reply reply) {
		return dao.replyInsert(reply);
	}
	//----------------[피드 작성 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[피드&댓글 좋아요 기능 시작]----------------------
	//특정 피드의 좋아요 클릭시 해당 피드의 좋아요를 추가하거나 취소하는 기능
	@Override
	public int feedLikeClick(int user_num, int feed_num) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("user_num", user_num);
		map.put("feed_num", feed_num);
		
		//해당 사용자가 이미 해당 피드의 좋아요를 준 적이 있는지 확인하는 메서드
		int methodResult = dao.feedLikeSelectOneForCheck(map);
		
		/*
		 * 결과가 1=이미 좋아요를 준 적이 있기에 해당 좋아요를 취소하는 delete문 실행
		 * 결과가 0=아직 좋아요를 준 적이 없기에 해당 피드에 좋아요를 주는 insert문 실행
		 */
		if(methodResult==0) {		//insert메서드를 실행하는 if문
			methodResult = dao.feedLikeInsertOne(map);
		}else {								//delete메서드를 실행하는 if문
			methodResult = dao.feedLikeDeleteOne(map);
		}
		log.debug("좋아요 줬는지 확인 : {}", methodResult);
		//삽입/삭제 메서드 실행후 변동된 좋아요 갯수를 다시 검색하여 리턴받는 메서드 실행
		int likeCount = dao.feedLikeCountSelectByFeedNum(feed_num);
		log.debug("좋아요 개수 확인 : {}", likeCount);
		return likeCount;
	}
	//피드 좋아요 여부 표시
	@Override
	public int feedLikePrint(int user_num, int feed_num) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("user_num", user_num);
		map.put("feed_num", feed_num);
		
		//해당 사용자가 이미 해당 피드의 좋아요를 준 적이 있는지 확인하는 메서드
		return dao.feedLikeSelectOneForCheck(map);
	}
	//특정 댓글의 좋아요 클릭시 해당 댓글에 좋아요를 추가하거나 취소하는 메서드
	@Override
	public int replyLikeClick(int user_num, int reply_num) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("user_num", user_num);
		map.put("reply_num", reply_num);
		
		//해당 사용자가 이미 해당 댓글에 좋아요를 준 적이 있는지 확인하는 메서드
		int methodResult = dao.replyLikeSelectOneForCheck(map);
		
		/*
		 * 결과가 1=이미 좋아요를 준 적이 있기에 해당 좋아요를 취소하는 delete문 실행
		 * 결과가 0=아직 좋아요를 준 적이 없기에 해당 피드에 좋아요를 주는 insert문 실행
		 */
		if(methodResult==0) {		//insert메서드를 실행하는 if문
			methodResult = dao.replyLikeInsertOne(map);
		}else {								//delete메서드를 실행하는 if문
			methodResult = dao.replyLikeDeleteOne(map);
		}
		
		//삽입/삭제 메서드 실행후 변동된 좋아요 갯수를 다시 검색하여 리턴받는 메서드 실행
		methodResult = dao.replyLikeCountSelectByFeedNum(reply_num);
		
		return methodResult;
	}
	//댓글 좋아요 여부 표시
	@Override
	public int replyLikePrint(int user_num, int reply_num) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("user_num", user_num);
		map.put("reply_num", reply_num);
		
		//해당 사용자가 이미 해당 피드의 좋아요를 준 적이 있는지 확인하는 메서드
		return dao.replyLikeSelectOneForCheck(map);
	}
	//----------------[피드&댓글 좋아요 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[태그 관련 기능 시작]----------------------
	//피드, 댓글에 달린 태그 중 하나를 클릭할경우, 해당 태그가 달린 피드를 검색해서 출력하는 메서드
	@Override
	public ArrayList<Feed> feedSearchByTagName(Map<String, String> tagMap){	
		//리턴받은 피드 배열을 service에 리턴
		return dao.feedArraySearchByTagName(tagMap);
	}
	
	@Override
	public int insertHashtag(String hashtag) {
		return dao.insertHashtag(hashtag);
		
	}
	@Override
	public int getTagNumByTagName(String hashtag) {
		return dao.getTagNumByTagName(hashtag);
	}
	@Override
	public int linkHashtagToFeed(Map<String, Object> hashtagLinkMap) {
		return dao.linkHashtagToFeed(hashtagLinkMap);
	}
	@Override
	public Member showOffcanvasWithUserData(String nickname) {
		return dao.memberInformationByNickname(nickname);
	}
	
	
	//----------------[태그 관련 기능 종료]----------->>>>>>>>>>>>
	
	
	//<<<<<<<<<<<------[삭제 관련 기능 시작]----------------------
	//해당 피드작성자인지를 확인한 후 해당 피드를 삭제하는 메서드
	@Override
	public int feedDeleteByUser(int feed_num, int user_num) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("feed_num", feed_num);
		map.put("user_num", user_num);
		
		return dao.feedDeleteByUser(map);
	}

	//해당 댓글 작성자인지를 확인 후 해당 댓글을 삭제하는 메서드
	@Override
	public int replyDeleteOne(int user_num, int reply_num) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("reply_num", reply_num);
		map.put("user_num", user_num);
		
		return dao.replyDeleteOne(map);
	}

	//삭제기능 실행 전 해당 피드가 자신의 피드인지 확인하는 메서드
	@Override
	public int feedSearch(int feed_num, int user_num) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("feed_num", feed_num);
		map.put("user_num", user_num);
		return dao.feedSearch(map);
	}

	//----------------[삭제 관련 기능 종료]----------->>>>>>>>>>>>
	
	//-----------------[기타기능] --------------->>>>>>>>>>>
	@Override
	public int saveMention(int replyId, List<Integer> mentionedUserIds) {
		return 1;
		
	}
	@Override
	public String findUserIdByNickname(String nickname) {
		Member member = new Member();
		member = dao.memberInformationByNickname(nickname);
		String user_id = member.getUser_id();
		return user_id;
	}
	@Override
	public int replyCountByFeedNum(int feed_num) {
		return dao.replyCountByFeedNum(feed_num);
	}

	//피드알림 목록을 호출하는 메서드
	@Override
	public ArrayList<FeedNotify> feedNotifySearchByUserNum(int user_num){
		return dao.feedNotifySearchByUserNum(user_num);
	}
	
}
