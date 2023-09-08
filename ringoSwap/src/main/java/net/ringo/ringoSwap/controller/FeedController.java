package net.ringo.ringoSwap.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Feed;
import net.ringo.ringoSwap.domain.FeedPhoto;
import net.ringo.ringoSwap.domain.FeedTag;
import net.ringo.ringoSwap.domain.Reply;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.FeedService;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.util.FileService;

@Slf4j
@Controller
@RequestMapping("feed")
public class FeedController {
	@Autowired
	FeedService service;

	@Autowired
	MemberService memberService;

	// Value : properties파일에 있는 걸 가져오기
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;

	// 피드서비스의 메인페이지로 이동하는 컨트롤러 메서드
	/*
	 * feedArrayType 변수 = 피드목록을 정렬할 때 어떤 방식으로 정렬할지 설정하는 변수. 이 변수를 페이지 이동시 주고받는 것으로
	 * 해당 페이지에서 피드목록으로 이동할때 자동적으로 설정된 변수로 출력되는 피드목록의 정렬형식을 변경합니다. 프론트엔드 구현시 정렬방식을
	 * 설정하는 변수를 이 변수명을 사용하면 좋겠습니다.
	 */
	@GetMapping("feedMain")
	public String feedMain(String feedArrayType) {
		if (feedArrayType == null || feedArrayType.equals("default") || feedArrayType.equals("popular")) {
			return "feed/feedMain"; // feedTypeAll 값이 잘못되어있으면 변수를 인계하지 않고 피드목록으로 이동한다
		}
		return "feedMain?feedArrayType=" + feedArrayType; // feedTypeAll 값이 'default' 혹은 'popular'일경우 해당 정렬방식을 피드목록 페이지에
															// 전달합니다.
	}

	/*
	 * 피드 목록 출력 controller 메서드(정렬방식:최근게시물/인기게시물)
	 * 
	 * @shl : join쓰지 않고 만들어야 된다는 것을 지금(6:00) 알았기 때문에 내일 이 메서드는 feed객체, 좋아요 수, 태그,
	 * 사진으로 4분할 시키도록 하겠습니다... (by 2023.08.29)
	 * 
	 * @shl : view는 신입니다 여러분~~~~ 위의 4분할 안건은 취소하고 모든 출력사항은 미리 만들어 놓은 view(sql.txt에
	 * create문을 추가했습니다)를 select하는 것으로 출력하도록 하겠습니다.(by 2023.08.30)
	 */
	@ResponseBody
	@PostMapping("feedPrintAll")
	public ArrayList<Feed> feedPrintAll(
			@RequestParam(name = "feedTypeAll", defaultValue = "default") String feedArrayType) {
		ArrayList<Feed> feedList = new ArrayList<>(); // 피드목록 출력용 배열 변수

		// 피드배열방식을 매개변수로 넘기고 배열방식에 따라 정렬된 게시글 목록을 리턴받는 메서드 실행
		feedList = service.feedSelectAllWithFeedArrayType(feedArrayType);
		log.debug("피드 리스트 확인 : {}", feedList);
		return feedList;
	}

	// 출력된 피드 목록에 따라 각 피드에 연결된 사진배열을 리턴하는 controller 메서드
	@ResponseBody
	@PostMapping("feedPhotoPrintAll")
	public ArrayList<FeedPhoto> feedPhotoPrintAll(ArrayList<Integer> feed_num) {
		ArrayList<FeedPhoto> photoList = new ArrayList<>();
		// 지정된 범위의 피드 목록의 사진을 전부 리턴받는 메서드 실행
		photoList = service.feedPhotoSelectArrayByFeedNum(feed_num);
		// 해당 사진 배열을 리턴
		return photoList;
	}

	// 특정 피드 게시글에서 해당 게시글의 정보를 리턴하는 controller 메서드
	@ResponseBody
	@PostMapping("feedPrint")
	public Feed feedPrint(int feed_num) {
		Feed feed = service.feedSelectOneByFeedNum(feed_num);
		return feed;
	}

	// 특정 피드 게시물 출력시 해당 피드와 같이 등록된 사진을 리턴해 출력하는 controller 메서드
	@ResponseBody
	@PostMapping("feedPhotoPrint")
	public ArrayList<FeedPhoto> feedPhotoPrint(int feed_num) {
		ArrayList<FeedPhoto> fphotoList = service.feedPhotoSelectByFeedNum(feed_num);

		return fphotoList;
	}

	// 특정 피드 게시글의 feed_num을 왜래키로 갖는 reply 배열을 리턴하는 controller 메서드
	@ResponseBody
	@PostMapping("replyPrint")
	public ArrayList<Reply> replyPrint(int feed_num) {
		ArrayList<Reply> replyList = service.replySelectByFeedNum(feed_num);

		return replyList;
	}

	// ----------------[피드 출력 기능 종료]----------->>>>>>>>>>>>

	// <<<<<<<<<<<------[피드 작성 기능 시작]----------------------

	/*
	 * 피드 작성 페이지에서 작성한 피드와 피드에 덧붙인 사진 정보를 매개변수로 하여 DB에 insert하고, insert 작업이 성공하면 피드의
	 * 메인 페이지로 이동하는 하는 메서드
	 */

	@ResponseBody
	@PostMapping("feedWrite")
	public ResponseEntity<?> feedWrite(@AuthenticationPrincipal UserDetails user,
			@RequestParam("content") String content
			, @RequestParam(value = "photos", required = false) MultipartFile[] photos) {
		Feed feed = new Feed();
		feed.setContents(content);
		log.debug("피드 데이터 확인 : {}", feed);
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		feed.setUser_num(user_num);
		int methodResult = service.feedInsert(feed);
		if (methodResult == 0) {
			return ResponseEntity.ok("fail");
		}
		
		int newFeedNum = feed.getFeed_num();
		log.debug("새로 생성된 feed_num: {}", newFeedNum);
		
		if (photos != null) {
	        ArrayList<FeedPhoto> photoList = new ArrayList<>();


		for (MultipartFile photo : photos) {
			if (photo != null && !photo.isEmpty()) {
				// 파일 메타데이터를 가져와서 FeedPhoto 객체를 생성합니다.
				FeedPhoto feedPhoto = new FeedPhoto();
				feedPhoto.setFeed_num(newFeedNum);
				feedPhoto.setPhoto_size((int) photo.getSize());
				feedPhoto.setPhoto_format(FilenameUtils.getExtension(photo.getOriginalFilename()));
				String savedfile = FileService.saveFile(photo, uploadPath);
				feedPhoto.setOrigin_file(photo.getOriginalFilename());
				feedPhoto.setSaved_file(savedfile);
				photoList.add(feedPhoto);
				log.debug("사진 데이터 확인 : {}", photoList);
			}
		}
		methodResult = service.feedPhotoInsert(photoList);		
		}
		
		return ResponseEntity.ok("Success");
	}

	// 특정 게시물에서 댓글을 작성하여 DB에 전달하는 controller 메서드
	@ResponseBody
	@PostMapping("replyInsert")
	public void replyInsert(@AuthenticationPrincipal UserDetails user, Reply reply) {
		int methodResult;
		methodResult = service.replyInsert(reply);
	}

	// ----------------[피드 작성 기능 종료]----------->>>>>>>>>>>>

	// <<<<<<<<<<<------[피드&댓글 좋아요 기능 시작]----------------------

	// 특정 피드의 좋아요 클릭시 해당 피드에 좋아요를 추가하거나 취소하는 기능
	@ResponseBody
	@PostMapping("feedLikeClicker")
	public int feedLikeClicker(@AuthenticationPrincipal UserDetails user, int feed_num) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());

		int methodResult = service.feedLikeClick(user_num, feed_num);

		return methodResult;
	}

	// 특정 댓글의 좋아요 클릭시 해당 댓글에 좋아요를 추가 혹은 취소하는 기능
	@ResponseBody
	@PostMapping("replyLikeClicker")
	public int replyLikeClicker(@AuthenticationPrincipal UserDetails user, int reply_num) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());

		int methodResult = service.replyLikeClick(user_num, reply_num);

		return methodResult;
	}

	// ----------------[피드&댓글 좋아요 기능 종료]----------->>>>>>>>>>>>

	// <<<<<<<<<<<------[태그 관련 기능 시작]----------------------

	// 피드, 댓글에 달린 태그 중 하나를 클릭할경우, 해당 태그가 달린 피드를 검색해서 출력하는 controller메서드
	@GetMapping("feedTagSearch")
	public ArrayList<Feed> feedTagSearch(String tag_name, String feedArrayType) {
		// service단에 검색한 태그명과 현재 정렬방식을 인계후 계산결과를 feed 배열로 리턴
		ArrayList<Feed> feedList = service.feedSearchByTagName(tag_name, feedArrayType);

		return feedList;
	}

	//

	// ----------------[태그 관련 기능 종료]----------->>>>>>>>>>>>

	
	
	// <<<<<<<<<<<------[검색 관련 기능 시작]----------------------

	
	//----------------[검색 관련 기능 종료]----------->>>>>>>>>>>>
	
	
	
	//<<<<<<<<<<<------[삭제 관련 기능 시작]----------------------
	@ResponseBody
	@PostMapping("feedDeleteOne")
	public String feedDeleteOne(int feed_num, @AuthenticationPrincipal UserDetails user) {
		String resultMsg = "";		//메서드 결과값에 따라 삭제여부를 기록하여 메서드 종료시 리턴되는 변수
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		log.debug("사용자 ID : {}", user.getUsername());
		log.debug("사용자 번호 : {}", user_num);
		
		/*
		 * 피드를 삭제하기 전 먼저 해당 피드에 사진이 있는지 검색하고, 사진이 없다면 resultMsg에 "피드 내 사진 없음"을 입력 후 진행합니다.
		 * 사진이 존재할경우, resultMsg에 "피드 내 사진 존재"를 입력후 해당 사진파일들의 삭제작업을 진행합니다.
		 * boolean타입 변수 d를 이용하여 삭제 기능 종료후 삭제가 정상적으로 진행되었는지를 확인합니다. 
		 * 삭제 작업이 정상적으로 진행되었을경우 "피드 내 사진 삭제성공"을, 삭제작업이 시행되지 않았을경우 "피드 내 사진 삭제실패"를
		 * resultMsg에 입력합니다.
		 */
		ArrayList<FeedPhoto> photoList = service.feedPhotoSelectByFeedNum(feed_num);
		log.debug("사진 : {}", photoList);
		log.debug("사진 : {}", photoList.isEmpty());
		if(photoList.isEmpty()) {
			resultMsg += "피드 내 사진 없음  /  ";
		}else {
			resultMsg += "피드 내 사진 존재  /  ";
			boolean d = false;
			for(FeedPhoto photo : photoList) {
				String deletefile = uploadPath + "/" + photo.getSaved_file();
				d = FileService.deleteFile(deletefile);		//기존 프로필 사진파일 삭제
				log.debug("삭제여부 : {}", d);
			}
			if(d) {
				resultMsg += "피드 내 사진 삭제완료  /  ";
			}else {
				resultMsg += "피드 내 사진 삭제실패  /  ";
			}
		}
		
		//사진 삭제 기능 종료후, 해당 피드를 삭제합니다. like, tag등은 전부 cascade로 묶여있으므로 피드 삭제시 같이 삭제됩니다.
		log.debug("삭제를 시작한다 : {}", user_num);
		int methodResult = service.feedDeleteByUser(feed_num, user_num);
		log.debug("삭제여부 : {}", methodResult);
		
		if(methodResult == 0) {
			//삭제기능이 정상적으로 실행되었는지 여부를 확인해, 0이라면 삭제가 실패되었음을 리턴합니다.
			resultMsg += "피드 삭제 실패";
			return resultMsg;
		}
		resultMsg += "피드 삭제 성공";		//0이 아니라면 삭제가 성공되었음을 의미하기에, 해당 문자열을 입력합니다.
		
		return resultMsg;		//현재까지의 삭제 메서드 결과를 리턴합니다.
	}

	
	//----------------[삭제 관련 기능 종료]----------->>>>>>>>>>>>
}