package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ringo.ringoSwap.dao.FeedDAO;
import net.ringo.ringoSwap.domain.Feed;
import net.ringo.ringoSwap.domain.FeedPhoto;
import net.ringo.ringoSwap.domain.Reply;

@Service
public class FeedServiceImple implements FeedService{
	@Autowired
	FeedDAO dao;

	//<<<<<<<<<<<------[피드 출력 기능 시작]----------------------
	//모든 게시물을 최신순으로 리턴하는 메서드
	@Override
	public ArrayList<Feed> feedSelectAll(){
		
		return dao.feedSelectAll();
	}
	//최근 게시물들을 인기순(좋아요 순)으로 리턴하는 메서드
	@Override
	public ArrayList<Feed> feedSelectPopularAll(){
		
		return dao.feedSelectPopularAll();
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
	//작성한 Reply 객체를 DB에 매개변수로 전달하는 메서드
	@Override
	public int replyInsert(Reply reply) {
		return dao.replyInsert(reply);
	}
}
