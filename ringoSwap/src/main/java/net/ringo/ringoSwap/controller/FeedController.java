package net.ringo.ringoSwap.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Feed;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.FeedService;

@Slf4j
@Controller
@RequestMapping("feed")
public class FeedController 
{
	@Autowired
	FeedService service;
	//피드서비스의 메인페이지로 이동하는 컨트롤러 메서드
	
	@GetMapping("feedMain")
	public String feedMain(String feedTypeAll)
	{
		if(feedTypeAll.isEmpty()) {
			return "feedMain";					//feedTypeAll 값이 없으면 변수를 인계하지 않고 피드목록으로 이동한다
		}
		return "feedMain?feedType="+feedTypeAll;			//feedTypeAll 값이 존재하면
	}

	//<<<<<<<<<<<------[피드 출력 기능 시작]----------------------
	//피드 목록 출력 controller 메서드(정렬방식:최근게시물/인기게시물)
	@ResponseBody
	@PostMapping("feedSelectAll")
	public ArrayList<Feed> feedSelectAll(@RequestParam(name="feedTypeAll", defaultValue = "default") String feedTypeAll) {
		ArrayList<Feed> feedList = new ArrayList<>();			//피드목록 출력용 배열 변수
		
		if(feedTypeAll.equals("default")) {					//피드타입이 'default'인 경우, 모든 게시글을 최신순으로 출력
			feedList = service.feedSelectAll();
		} else if(feedTypeAll.equals("interest")) {			//피드타입이 'interest'인 경우, 모든 게시글을 인기순으로 출력
			feedList = service.feedSelectPopularAll();
		}
		
		return feedList;
	}
	//특정 피드 게시글 페이지로 이동하는 controller 메서드
	@GetMapping("feedRead")
	public String feedRead(int feed_num,  String feedTypeAll) {
		if(feed_num <=  0) {		//잘못된 feed 경로로 진입하면 되돌아가는 메서드
			return "feedMain?feedType="+feedTypeAll;
		}
		
		return "feedRead?feed_num="+feed_num;
	}
	//특정 피드 게시글에서 해당 게시글의 정보를 출력하는 controller 메서드
	@ResponseBody
	@PostMapping("feedSelectOne")
	public Feed feedSelectOne(int feed_num) {
		Feed feed = service.feedSelectOne(feed_num);
		
		return null;
	}
	
	//----------------[피드 출력 기능 종료]----------->>>>>>>>>>>>

}