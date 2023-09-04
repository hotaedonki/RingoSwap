package net.ringo.ringoSwap.controller;

import java.util.ArrayList;
import java.util.HashMap;


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
import net.ringo.ringoSwap.service.MemberService;

@Slf4j
@Controller
@RequestMapping("feed")
public class FeedController 
{
	@Autowired
	FeedService service;

	@Autowired
	MemberService memberService;
	
	//피드서비스의 메인페이지로 이동하는 컨트롤러 메서드
	/*
	 * feedArrayType 변수 = 피드목록을 정렬할 때 어떤 방식으로 정렬할지 설정하는 변수.
	 * 이 변수를 페이지 이동시 주고받는 것으로 해당 페이지에서 피드목록으로 이동할때 자동적으로
	 * 설정된 변수로 출력되는 피드목록의 정렬형식을 변경합니다.
	 * 프론트엔드 구현시 정렬방식을 설정하는 변수를 이 변수명을 사용하면 좋겠습니다.
	 */
	@GetMapping("feedMain")
	public String feedMain(String feedArrayType)
	{
		if(feedArrayType == null || feedArrayType.equals("default") || feedArrayType.equals("popular")) {
			return "feed/feedMain";					//feedTypeAll 값이 잘못되어있으면 변수를 인계하지 않고 피드목록으로 이동한다
		}
		return "feedMain?feedArrayType="+feedArrayType;			//feedTypeAll 값이 'default' 혹은 'popular'일경우 해당 정렬방식을 피드목록 페이지에 전달합니다.
	}
	
	//특정 피드 게시글 페이지로 이동하는 controller 메서드
	@GetMapping("feedRead")
	public String feedRead(int feed_num,  String feedArrayType) {
		if(feed_num <=  0) {		//잘못된 feed 경로로 진입하면 되돌아가는 메서드
			return "feedMain?feedArrayType="+feedArrayType;
		}
		
		return "feedRead?feedArrayType="+feedArrayType+"&feed_num="+feed_num;
	}
	
	//피드를 작성하는 페이지로 이동하는 controller 메서드
	@GetMapping("feedWrite")
	public String feedWrite(String feedArrayType) {
		return "feed/feedWrite?feedArrayType="+feedArrayType;
	}

	//<<<<<<<<<<<------[피드 출력 기능 시작]----------------------
	
	/*
	 * 피드 목록 출력 controller 메서드(정렬방식:최근게시물/인기게시물)
	 * @shl : join쓰지 않고 만들어야 된다는 것을 지금(6:00) 알았기 때문에 
	 * 		내일 이 메서드는 feed객체, 좋아요 수, 태그, 사진으로 4분할 시키도록 하겠습니다...
	 * 		(by 2023.08.29)
	 *  @shl : view는 신입니다 여러분~~~~
	 * 		위의 4분할 안건은 취소하고 모든 출력사항은 미리 만들어 놓은 view(sql.txt에 create문을 추가했습니다)를
	 * 		select하는 것으로 출력하도록 하겠습니다.(by 2023.08.30)
	 */
	@ResponseBody
	@PostMapping("feedPrintAll")
	public ArrayList<Feed> feedPrintAll(@RequestParam(name="feedTypeAll", defaultValue = "default") String feedArrayType) {
		ArrayList<Feed> feedList = new ArrayList<>();			//피드목록 출력용 배열 변수
		
		//피드배열방식을 매개변수로 넘기고 배열방식에 따라 정렬된 게시글 목록을 리턴받는 메서드 실행
		feedList = service.feedSelectAllWithFeedArrayType(feedArrayType);
		
		return feedList;
	}
	//출력된 피드 목록에 따라 각 피드에 연결된 사진배열을 리턴하는 controller 메서드
	@ResponseBody
	@PostMapping("feedPhotoPrintAll")
	public ArrayList<FeedPhoto> feedPhotoPrintAll(ArrayList<Integer> feed_num){
		ArrayList<FeedPhoto> photoList = new ArrayList<>();
		//지정된 범위의 피드 목록의 사진을 전부 리턴받는 메서드 실행
		photoList = service.feedPhotoSelectArrayByFeedNum(feed_num);
		//해당 사진 배열을 리턴
		return photoList;
	}
	//특정 피드 게시글에서 해당 게시글의 정보를 리턴하는 controller 메서드
	@ResponseBody
	@PostMapping("feedPrint")
	public Feed feedPrint(int feed_num) {
		Feed feed = service.feedSelectOneByFeedNum(feed_num);
		return feed;
	}
	//특정 피드 게시물 출력시 해당 피드와 같이 등록된 사진을 리턴해 출력하는 controller 메서드
	@ResponseBody
	@PostMapping("feedPhotoPrint")
	public ArrayList<FeedPhoto> feedPhotoPrint(int feed_num){
		ArrayList<FeedPhoto> fphotoList = service.feedPhotoSelectByFeedNum(feed_num);
		
		return fphotoList;
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
	
	/*
	 * 피드 작성 페이지에서 작성한 피드와 피드에 덧붙인 사진 정보를 매개변수로 하여 DB에 insert하고, 
	 * insert 작업이 성공하면 피드의 메인 페이지로 이동하는 하는 메서드
	 */
	@PostMapping("feedWrite")
	public String feedWrite(@AuthenticationPrincipal UserDetails user
					, Feed feed, ArrayList<FeedPhoto> photo, String feedArrayType) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		feed.setUser_num(user_num);
		int methodResult = service.feedInsert(feed);
		if(methodResult == 0) {
			return "redirect:/feed/feedWrite";
		}
		methodResult = service.feedPhotoInsert(photo);
		
		return "redirect:/feed/feedMain?feedArrayType="+feedArrayType;
	}
	
	
	// 특정 게시물에서 댓글을 작성하여 DB에 전달하는 controller 메서드
	@ResponseBody
	@PostMapping("replyInsert")
	public void replyInsert(@AuthenticationPrincipal UserDetails user
					, Reply reply) {
		int methodResult;
		methodResult = service.replyInsert(reply);
	}
	
	//----------------[피드 작성 기능 종료]----------->>>>>>>>>>>>
	
	
	
	//<<<<<<<<<<<------[피드&댓글 좋아요 기능 시작]----------------------

	//특정 피드의 좋아요 클릭시 해당 피드에 좋아요를 추가하거나 취소하는 기능
	@ResponseBody
	@PostMapping("feedLikeClicker")
	public int feedLikeClicker(@AuthenticationPrincipal UserDetails user
					, int feed_num) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		
		int methodResult = service.feedLikeClick(user_num, feed_num);
		
		return methodResult;
	}

	//특정 댓글의 좋아요 클릭시 해당 댓글에 좋아요를 추가 혹은 취소하는 기능
	@ResponseBody
	@PostMapping("replyLikeClicker")
	public int replyLikeClicker(@AuthenticationPrincipal UserDetails user
					, int reply_num) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		
		int methodResult = service.replyLikeClick(user_num, reply_num);
		
		return methodResult;
	}
	
	//----------------[피드&댓글 좋아요 기능 종료]----------->>>>>>>>>>>>

	
	
	//<<<<<<<<<<<------[태그 관련 기능 시작]----------------------
	
	//피드, 댓글에 달린 태그 중 하나를 클릭할경우, 해당 태그가 달린 피드를 검색해서 출력하는 controller메서드
	@GetMapping("feedTagSearch")
	public ArrayList<Feed> feedTagSearch(String tag_name, String feedArrayType){
		//service단에 검색한 태그명과 현재 정렬방식을 인계후 계산결과를 feed 배열로 리턴
		ArrayList<Feed> feedList = service.feedSearchByTagName(tag_name, feedArrayType);
		
		return feedList;
	}
	
	//
	
	//----------------[태그 관련 기능 종료]----------->>>>>>>>>>>>

	
	
	//<<<<<<<<<<<------[검색 관련 기능 시작]----------------------
	

	
	//----------------[검색 관련 기능 종료]----------->>>>>>>>>>>>
}