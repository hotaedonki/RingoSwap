package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ringo.ringoSwap.dao.FeedDAO;
import net.ringo.ringoSwap.domain.Feed;

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
	//----------------[피드 출력 기능 종료]----------->>>>>>>>>>>>
}
