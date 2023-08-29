package net.ringo.ringoSwap.dao;


import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.Feed;


@Mapper
public interface FeedDAO 
{

	//<<<<<<<<<<<------[피드 출력 기능 시작]----------------------
	//모든 게시물을 최신순으로 리턴하는 메서드
	ArrayList<Feed> feedSelectAll();
	//최근 게시물들을 인기순(좋아요 순)으로 리턴하는 메서드
	ArrayList<Feed> feedSelectPopularAll();
	
	//----------------[피드 출력 기능 종료]----------->>>>>>>>>>>>
	
}