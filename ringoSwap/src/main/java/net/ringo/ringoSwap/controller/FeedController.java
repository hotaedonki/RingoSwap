package net.ringo.ringoSwap.controller;

import java.util.ArrayList;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Feed;
import net.ringo.ringoSwap.domain.FeedPhoto;
import net.ringo.ringoSwap.domain.FeedTag;
import net.ringo.ringoSwap.domain.Reply;
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
		if(feedTypeAll.equals("default") || feedTypeAll.equals("popular")) {
			return "feedMain";					//feedTypeAll 값이 잘못되어있으면 변수를 인계하지 않고 피드목록으로 이동한다
		}
		return "feedMain?feedType="+feedTypeAll;			//feedTypeAll 값이 'default' 혹은 'popular'일경우 해당 정렬방식을 피드목록 페이지에 전달합니다.
	}

	//<<<<<<<<<<<------[피드 출력 기능 시작]----------------------
	/*
	 * 피드 목록 출력 controller 메서드(정렬방식:최근게시물/인기게시물)
	 * @shl : join쓰지 않고 만들어야 된다는 것을 지금(6:00) 알았기 때문에 
	 * 		내일 이 메서드는 feed객체, 좋아요 수, 태그, 사진으로 4분할 시키도록 하겠습니다...
	 * 		(by 2023.08.29)
	 */
	@ResponseBody
	@PostMapping("feedPrintAll")
	public ArrayList<Feed> feedPrintAll(@RequestParam(name="feedTypeAll", defaultValue = "default") String feedTypeAll) {
		ArrayList<Feed> feedList = new ArrayList<>();			//피드목록 출력용 배열 변수
		
		if(feedTypeAll.equals("default")) {					//피드타입이 'default'인 경우, 모든 게시글을 최신순으로 출력
			feedList = service.feedSelectAll();
		} else if(feedTypeAll.equals("popular")) {			//피드타입이 'interest'인 경우, 모든 게시글을 인기순으로 출력
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
	//특정 피드 게시물 출력시 해당 피드와 같이 등록된 사진을 리턴해 출력하는 controller 메서드
	@ResponseBody
	@PostMapping("feedPhotoPrint")
	public ArrayList<FeedPhoto> feedPhotoPrint(int feed_num){
		ArrayList<FeedPhoto> fphotoList = service.feedPhotoSelectByFeedNum(feed_num);
		
		return fphotoList;
	}
	
	//특정 피드 게시글에서 해당 게시글의 정보를 리턴하는 controller 메서드
	@ResponseBody
	@PostMapping("feedPrint")
	public Feed feedPrint(int feed_num) {
		Feed feed = service.feedSelectOneByFeedNum(feed_num);
		return feed;
	}
	//특정 피드 게시글의 feed_num을 왜래키로 갖는 reply 배열을 리턴하는 controller 메서드
	@ResponseBody
	@PostMapping("replyPrint")
	public ArrayList<Reply> replyPrint(int feed_num){
		ArrayList<Reply> replyList = service.replySelectByFeedNum(feed_num);
		
		return replyList;
	}
	//----------------[피드 출력 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[피드 작성 기능 시작]----------------------
	// 특정 게시물에서 댓글을 작성하여 DB에 전달하는 controller 메서드
	@ResponseBody
	@PostMapping("replyInsert")
	public void replyInsert(@AuthenticationPrincipal UserDetails user
					, Reply reply) {
		int methodResult;
		methodResult = service.replyInsert(reply);
	}
}