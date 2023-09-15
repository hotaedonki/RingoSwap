package net.ringo.ringoSwap.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Feed;
import net.ringo.ringoSwap.domain.FeedPhoto;
import net.ringo.ringoSwap.domain.Member;
import net.ringo.ringoSwap.domain.MemberFollow;
import net.ringo.ringoSwap.domain.Reply;
import net.ringo.ringoSwap.domain.Tagstorage;
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
	public Map<String, Object> feedPrintAll(
			@AuthenticationPrincipal UserDetails user
			, @RequestParam(name = "feedArrayType", defaultValue = "default") String feedArrayType
			, @RequestParam(name="text", defaultValue = "null") String text) {
		Map<String, Object> map = new HashMap<>();
	    ArrayList<Feed> feedList = new ArrayList<>(); // 피드목록 출력용 배열 변수
	    int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());

	    // 피드배열방식을 매개변수로 넘기고 배열방식에 따라 정렬된 게시글 목록을 리턴받는 메서드 실행
	    log.debug("확인?{}");
	    feedList = service.feedSelectAllWithFeedArrayType(feedArrayType, text);
	    log.debug("확인완료 {}", feedList);

	    // 각 피드에 대한 좋아요 클릭 여부를 저장할 Map
	    Map<Integer, Integer> likeCheckMap = new HashMap<>();
	    for (Feed feed : feedList) {
	        int feed_num = feed.getFeed_num();
	        // 현재 피드에 대한 좋아요 클릭 여부를 확인
	        int likeCheck = service.feedLikePrint(user_num, feed_num);
	        // 결과를 Map에 저장
	        likeCheckMap.put(feed_num, likeCheck);
	    }

	    log.debug("피드 리스트 확인 : {}", feedList);
	    map.put("feedList", feedList);
	    map.put("likeCheckMap", likeCheckMap); // 좋아요 클릭 여부 Map을 결과 Map에 추가
	    return map;
	}

	// 특정 피드 게시글에서 해당 게시글의 정보를 리턴하는 controller 메서드
	@ResponseBody
	@PostMapping("feedPrint")
	public Map<String, Object> feedPrint(
	        @AuthenticationPrincipal UserDetails user,
	        int feed_num) {
	    int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
	    Feed feed = service.feedSelectOneByFeedNum(feed_num);
	    
	    int likeCheck = service.feedLikePrint(user_num, feed_num); // 좋아요 체크 값을 가져옵니다.

	    Map<String, Object> map = new HashMap<>();
	    map.put("feed", feed);
	    map.put("likeCheck", likeCheck); // 좋아요 체크 값을 response에 추가합니다.

	    return map;
	}

	// 특정 피드 게시물 출력시 해당 피드와 같이 등록된 사진을 리턴해 출력하는 controller 메서드
	@PostMapping("feedPhotoPrint")
	@ResponseBody
	public List<Map<String, Object>> feedPhotoPrint(@RequestParam Integer feed_num, HttpServletRequest request,
			HttpServletResponse response) {
		List<FeedPhoto> photoList = service.feedPhotoSelectByFeedNum(feed_num);
		List<Map<String, Object>> responseData = new ArrayList<>();
		log.debug("피드넘확인 : {}", feed_num);
		for (FeedPhoto photo : photoList) {
			String fullPath = uploadPath + "/" + photo.getSaved_file();

			File file = new File(fullPath);
			if (file.exists()) {
				try {
					byte[] byteArr = Files.readAllBytes(file.toPath());
					Map<String, Object> photoData = new HashMap<>();
					photoData.put("fileName", photo.getOrigin_file());
					photoData.put("fileData", Base64.getEncoder().encodeToString(byteArr));

					responseData.add(photoData);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			log.debug("포토넘확인 : {}", photo.getPhoto_num());
		}

		return responseData;
	}

	// 특정 피드 게시글의 feed_num을 왜래키로 갖는 reply 배열을 리턴하는 controller 메서드
	@ResponseBody
	@PostMapping("replyPrint")
	public Map<String, Object> replyPrint(int feed_num, @AuthenticationPrincipal UserDetails user) {
		ArrayList<Reply> replyList = service.replySelectByFeedNum(feed_num);		//해당 피드의 댓글목록 정보를 담는 변수
		HashMap<Integer, Integer> likeCheckMap = new HashMap<>();					//각 피트의 좋아요 여부 정보를 담는 변수
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());	//likeCheckMap에 사용할 회원 번호

		//likeCheckMap에 replyList배열에 저장된 각 댓글의 사용자의 좋아요 여부 정보를 기록한다.
		for (Reply reply : replyList) {
	        int reply_num = reply.getReply_num();
			int likeCheck = service.replyLikePrint(user_num, reply_num); // 좋아요 체크 값을 가져옵니다.
			likeCheckMap.put(reply_num, likeCheck);
		}
		
		HashMap<String, Object> map = new HashMap<>();//리턴용 해시맵 변수
		map.put("replyList", replyList);
		map.put("likeCheckMap", likeCheckMap);
		return map;
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
			@RequestParam("content") String content,
			@RequestParam(value = "photos", required = false) MultipartFile[] photos, 
			@RequestParam(value = "hashtagsJson", required = false) String hashtagsJson) {
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
		
		log.debug("해시태그 값: {}", hashtagsJson);
		
		//해시태그 저장
		if (hashtagsJson != null && !hashtagsJson.trim().isEmpty()) {
			//Json 문자열을 Java객체로 변환함, 첫번째 객체를 두번째 객체로 변환
	        String[] hashtags = new Gson().fromJson(hashtagsJson, String[].class);
	        log.debug("존슨에 들어왔는지: {}", hashtags[0]);
	        int linkNum = 0;
	        for (String hashtag : hashtags) {
	        	hashtag = hashtag.substring(1);
	        	int insertHashtag = service.insertHashtag(hashtag); // 해시태그가 DB에 없으면 삽입
	            int tagNum = service.getTagNumByTagName(hashtag); // 해시태그 번호 가져오기
	            int linkHashtag = service.linkHashtagToFeed(newFeedNum, tagNum, linkNum); // 해시태그와 피드 연결
	            linkNum++;
	        	log.debug("해시태그: 입력 성공: {}, 해시태그 넘버: {}, 피드랑 연동됐는지: {}", insertHashtag, tagNum, linkHashtag, linkNum);
	            }
	        }

		if (photos != null && photos.length > 0) {
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
					service.feedPhotoInsert(feedPhoto);
				}
			}
		}

		return ResponseEntity.ok("Success");
	}

	// 특정 게시물에서 댓글을 작성하여 DB에 전달하는 controller 메서드
	@ResponseBody
	@PostMapping("replyInsert")
	public int replyInsert(@AuthenticationPrincipal UserDetails user
			, String contents
			, int feed_num
			, int parent_reply_num
			, @RequestParam(value = "mentioned_user_num", required = false) List<String> mentionedUsers) {
		log.debug("리플내용확인 {} ", contents);
		log.debug("리플 피드넘 {} ", feed_num);
		Reply reply = new Reply();
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		reply.setUser_num(user_num);
		reply.setContents(contents);
		reply.setFeed_num(feed_num);
		reply.setParent_reply_num(parent_reply_num);
		int methodResult = service.replyInsert(reply);

		int replyId = reply.getReply_num();
		
		if (mentionedUsers != null && !mentionedUsers.isEmpty()) {
	        List<Integer> mentionedUserIds = mentionedUsers.stream()
	                .map(mentionedUser -> mentionedUser.substring(1)) // @ 제거
	                .map(username -> memberService.getUserIdByUsername(username)) // NAME으로 유저NUM 조회
	                .filter(Objects::nonNull) //유저 ID가 NULL이 아닌 경우만
	                .collect(Collectors.toList()); // 결과를 LIST로
	                
	        if (!mentionedUserIds.isEmpty()) {
	            service.saveMention(replyId, mentionedUserIds);
	        }
	    }
		
		return reply.getFeed_num();
	}

	// ----------------[피드 작성 기능 종료]----------->>>>>>>>>>>>

	// <<<<<<<<<<<------[피드&댓글 좋아요 기능 시작]----------------------

	// 특정 피드의 좋아요 클릭시 해당 피드에 좋아요를 추가하거나 취소하는 기능
	@ResponseBody
	@PostMapping("feedLikeClicker")
	public int feedLikeClicker(@AuthenticationPrincipal UserDetails user, int feed_num) {
		log.debug("좋아요 개수 확인 피드 {}", feed_num);
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		int methodResult = service.feedLikeClick(user_num, feed_num);
		log.debug("좋아요 개수 by 컨트롤러 : {}, ", methodResult);
		return methodResult;
	}

	// 특정 댓글의 좋아요 클릭시 해당 댓글에 좋아요를 추가 혹은 취소하는 기능
	@ResponseBody
	@PostMapping("replyLikeClicker")
	public int replyLikeClicker(@AuthenticationPrincipal UserDetails user, int reply_num) {
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		log.debug("ID번호 {}", user_num);
		log.debug("댓글번호 {}", reply_num);
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

	// <<<<<<<<<<<------[팔로워/팔로우 검색 관련 기능 시작]----------------------
	//닉네임을 매개변수로 하는 팔로워 검색 메서드
	@ResponseBody
	@PostMapping("followerSearch")
	public ArrayList<MemberFollow> followerSearch(String username
			, @AuthenticationPrincipal UserDetails user){
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		HashMap<String, Object> map = new HashMap<>();		//followerArraySearch메서드용 해쉬맵 변수
		ArrayList<Integer> followerList = memberService.memberByUsernameReturnUserNum(username); 
		if(followerList.isEmpty()) {		//followerList변수가 비어있음 = 검색된 팔로워가 없는 것이므로 이후 검색 메서드 구동은 스킵한다.
			ArrayList<MemberFollow> none = null;
			return none;
		}
		
		map.put("followee_num", user_num);
		map.put("follower_num", followerList);
		
		ArrayList<MemberFollow> followerSearch = memberService.followerArraySearch(map);
		
		return followerSearch;
	}

	//닉네임을 매개변수로 하는 팔로우 검색 메서드
	@ResponseBody
	@PostMapping("followeeSearch")
	public ArrayList<MemberFollow> followeeSearch(String username
					, @AuthenticationPrincipal UserDetails user){
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		HashMap<String, Object> map = new HashMap<>();		//followerArraySearch메서드용 해쉬맵 변수
		ArrayList<Integer> followeeList = memberService.memberByUsernameReturnUserNum(username); 
		if(followeeList.isEmpty()) {		//followeeList변수가 비어있음 = 검색된 팔로우 회원이 없는 것이므로 이후 검색 메서드 구동은 스킵한다.
			ArrayList<MemberFollow> none = null;
			return none;
		}
		map.put("follower_num", user_num);
		map.put("followee_num", followeeList);
		
		ArrayList<MemberFollow> followeeSearch = memberService.followeeArraySearch(map);
		
		return followeeSearch;
	}
	//팔로우 했는지 여부를 확인하는 메서드
	@ResponseBody
	@PostMapping("followCheck")
	public ResponseEntity<?> followCheck(@AuthenticationPrincipal UserDetails user
					, String username){
		int user_num = memberService.getUserIdByUsername(username);
		HashMap<String, Object> map = new HashMap<>();
		return ResponseEntity.ok("success");
	}
	//사용자가 특정 회원을 팔로우 하는 기능
	@ResponseBody
	@PostMapping("userFollowInsert")
	public ResponseEntity<?> userFollowInsert(int user_num
					, @AuthenticationPrincipal UserDetails user) {
		int follower_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		
		int methodResult = memberService.followInsert(user_num, follower_num);		//팔로우 추가 메서드 실행
		if(methodResult == 0) {
			return ResponseEntity.ok("fail");
		}
		
		return ResponseEntity.ok("success");
	}
	//사용자가 특정 회원을 언팔로우 하는 기능
	@ResponseBody
	@PostMapping("userFollowDelete")
	public ResponseEntity<?> userFollowDelete(int user_num
					, @AuthenticationPrincipal UserDetails user) {
		int follower_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		
		int methodResult = memberService.followDelete(user_num, follower_num);		//팔로우 해제 메서드 실행
		if(methodResult == 0) {
			return ResponseEntity.ok("fail");
		}
		
		return ResponseEntity.ok("success");
	}
	// ----------------[팔로워/팔로우 검색 관련 기능 종료]----------->>>>>>>>>>>>

	// <<<<<<<<<<<------[삭제 관련 기능 시작]----------------------
	@ResponseBody
	@PostMapping("feedDeleteOne")
	public String feedDeleteOne(int feed_num
			, @AuthenticationPrincipal UserDetails user) {
		String resultMsg = ""; // 메서드 결과값에 따라 삭제여부를 기록하여 메서드 종료시 리턴되는 변수
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());

		/*
		 * 피드를 삭제하기 전 먼저 해당 피드에 사진이 있는지 검색하고, 사진이 없다면 resultMsg에 "피드 내 사진 없음"을 입력 후
		 * 진행합니다. 사진이 존재할경우, resultMsg에 "피드 내 사진 존재"를 입력후 해당 사진파일들의 삭제작업을 진행합니다.
		 * boolean타입 변수 d를 이용하여 삭제 기능 종료후 삭제가 정상적으로 진행되었는지를 확인합니다. 삭제 작업이 정상적으로 진행되었을경우
		 * "피드 내 사진 삭제성공"을, 삭제작업이 시행되지 않았을경우 "피드 내 사진 삭제실패"를 resultMsg에 입력합니다.
		 */
		ArrayList<FeedPhoto> photoList = service.feedPhotoSelectByFeedNum(feed_num);
		log.debug("사진 : {}", photoList);
		log.debug("사진 : {}", photoList.isEmpty());
		if (photoList.isEmpty()) {
			resultMsg += "피드 내 사진 없음  /  ";
		} else {
			resultMsg += "피드 내 사진 존재  /  ";
			boolean d = false;
			for (FeedPhoto photo : photoList) {
				String deletefile = uploadPath + "/" + photo.getSaved_file();
				d = FileService.deleteFile(deletefile); // 기존 프로필 사진파일 삭제
				log.debug("삭제여부 : {}", d);
			}
			if (d) {
				resultMsg += "피드 내 사진 삭제완료  /  ";
			} else {
				resultMsg += "피드 내 사진 삭제실패  /  ";
			}
		}

		// 사진 삭제 기능 종료후, 해당 피드를 삭제합니다. like, tag등은 전부 cascade로 묶여있으므로 피드 삭제시 같이 삭제됩니다.
		log.debug("삭제를 시작한다 : {}", user_num);
		int methodResult = service.feedDeleteByUser(feed_num, user_num);
		log.debug("삭제여부 : {}", methodResult);

		if (methodResult == 0) {
			// 삭제기능이 정상적으로 실행되었는지 여부를 확인해, 0이라면 삭제가 실패되었음을 리턴합니다.
			resultMsg = "0";
			return resultMsg;
		}
		resultMsg = "1"; // 0이 아니라면 삭제가 성공되었음을 의미하기에, 해당 문자열을 입력합니다.

		return resultMsg; // 현재까지의 삭제 메서드 결과를 리턴합니다.
	}

	@ResponseBody
	@PostMapping("replyDeleteOne")
	public String replyDeleteOne(int reply_num
					, @AuthenticationPrincipal UserDetails user) {
		String resultMsg="";		//댓글삭제 메서드 실행결과를 담아 리턴하는 문자열 변수.
		int user_num = memberService.memberSearchByIdReturnUserNum(user.getUsername());
		
		//삭제 메서드 실행
		int methodResult = service.replyDeleteOne(user_num, reply_num);
		
		if(methodResult == 0) {
			resultMsg = "댓글 삭제 실패";
			return resultMsg;
		}
		resultMsg = "댓글 삭제 성공";
		return resultMsg;
	}
	
	// ----------------[삭제 관련 기능 종료]----------->>>>>>>>>>>>
}