package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.multipart.MultipartFile;

import net.ringo.ringoSwap.domain.Feed;
import net.ringo.ringoSwap.domain.FeedPhoto;
import net.ringo.ringoSwap.domain.Reply;

public interface FeedService {

	//<<<<<<<<<<<------[피드 출력 기능 시작]----------------------
	//모든 게시물을 매개변수로 받은 배열방식에 따라 최신순, 혹은 인기순(좋아요 순)으로 리턴하는 메서드
	public ArrayList<Feed> feedSelectAllWithFeedArrayType(String feedArrayType);
	//지정된 피드 목록의 피드 번호 배열을 매개변수로 주고, 그에 따른 피드 사진 배열을 리턴하는 메서드
	public ArrayList<FeedPhoto> feedPhotoSelectArrayByFeedNum(ArrayList<Integer> feed_num);
	
	//feed_num을 매개변수로 특정 피드 게시글 정보를 리턴하는 메서드
	public Feed feedSelectOneByFeedNum(int feed_num);
	//feed_num을 매개변수로 특정 댓글 배열 정보를 리턴하는 메서드
	public ArrayList<Reply> replySelectByFeedNum(int feed_num);
	//feed_num을 매개변수로 해당 피드에 등록된 사진 정보 배열 객체를 불러오는 메서드
	public ArrayList<FeedPhoto> feedPhotoSelectByFeedNum(int feed_num);
	//----------------[피드 출력 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[피드 작성 기능 시작]----------------------
	//작성한 feed 객체를 DB에 매개변수로 전달하는 메서드
	public int feedInsert(Feed feed);
	//작성한 feedPhoto 배열을 DB에 매개변수로 전달하는 메서드
	public int feedPhotoInsert(ArrayList<FeedPhoto> photoList);
	//작성한 Reply 객체를 DB에 매개변수로 전달하는 메서드
	public int replyInsert(Reply reply);
	//----------------[피드 작성 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[피드&댓글 좋아요 기능 시작]----------------------
	//특정 피드의 좋아요 클릭시 해당 피드에 좋아요를 추가하거나 취소하는 메서드
	public int feedLikeClick(int user_num, int feed_num);
	//특정 댓글의 좋아요 클릭시 해당 댓글에 좋아요를 추가하거나 취소하는 메서드
	public int replyLikeClick(int user_num, int reply_num);
	//----------------[피드&댓글 좋아요 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[태그 관련 기능 시작]----------------------
	//피드, 댓글에 달린 태그 중 하나를 클릭할경우, 해당 태그가 달린 피드를 검색해서 출력하는
	public ArrayList<Feed> feedSearchByTagName(String tag_name, String feedArrayType);
	
	
	
	//----------------[태그 관련 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[삭제 관련 기능 시작]----------------------
	//해당 피드작성자인지를 확인한 후 해당 피드를 삭제하는 메서드
	public int feedDeleteByUser(int feed_num, int user_num);
	
	//----------------[삭제 관련 기능 종료]----------->>>>>>>>>>>>
}
