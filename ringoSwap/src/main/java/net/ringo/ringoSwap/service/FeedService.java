package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import net.ringo.ringoSwap.domain.Feed;

public interface FeedService {

	//<<<<<<<<<<<------[피드 출력 기능 시작]----------------------
	//모든 게시물을 최신순으로 리턴하는 메서드
	public ArrayList<Feed> feedSelectAll();
	//최근 게시물들을 인기순(좋아요 순)으로 리턴하는 메서드
	public ArrayList<Feed> feedSelectPopularAll();
	//----------------[피드 출력 기능 종료]----------->>>>>>>>>>>>

}
