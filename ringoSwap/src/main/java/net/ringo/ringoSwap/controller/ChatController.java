package net.ringo.ringoSwap.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
import net.ringo.ringoSwap.domain.DM_Chatroom;
import net.ringo.ringoSwap.domain.Member;
import net.ringo.ringoSwap.domain.custom.ChatroomThumbnail;
import net.ringo.ringoSwap.domain.custom.OpenChatroomInfo;
import net.ringo.ringoSwap.enums.webService.MessageType;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.util.PageNavigator;
import net.ringo.ringoSwap.util.PathHandler;

@Slf4j
@Controller
@RequestMapping(PathHandler.CHAT)
@RequiredArgsConstructor
public class ChatController 
{	
	@Autowired
	ChatService service;
	
	@Autowired
	MemberService mService;
	
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;
	//채팅방 목록의 페이지당 글 수
	@Value("${user.chatting.page}")
	int countPerPage;
	//채팅방 목록의 페이지 이동 링크 수
	@Value("${user.board.pageGroup}")
	int pagePerGroup;
	
	Map<String, Map<String, Object>> tokensUsersInfoDMChat = new HashMap<>();
	Map<String, DM_Chatroom> tokensCreateDMChat = new HashMap<>();
	
	//채팅서비스의 메인페이지로 이동하는 컨트롤러 메서드
	@GetMapping(PathHandler.OPENCHATMAIN)
	public String chatMain(String lang_category, Model model, @AuthenticationPrincipal UserDetails user
					, @RequestParam(name="page", defaultValue ="1" ) int page)
	{
		log.debug("move to chat/openChatMain . . .");
		
		if (user == null || user.getUsername() == null || user.getUsername().length() <= 0)
		{
			return "memberView/home";
		}
		
		PageNavigator navi = service.chatRoomPageNavigator(pagePerGroup, countPerPage, page);
		
		// user의 이름으로 user_num을 가져온다.
		int userNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
		
		ArrayList<OpenChatroomInfo> openChatrooms = new ArrayList<>();
		
		// 언어 필터 관련해서 값이 있으면
		if (lang_category != null && (lang_category.equals("ko") || lang_category.equals("en") || lang_category.equals("ja")))
			openChatrooms = service.searchChatroomByLang(navi, lang_category);
		else
			openChatrooms = service.getAllOpenchatrooms(navi);
		
		// chatRoomNums이 있는 경우(= 한개 이상 링크(ringo_chatroom_link)가 있는 경우) chat room의 정보를 가져온다.
		if (openChatrooms != null && openChatrooms.size() > 0)
		{
			model.addAttribute("openChatrooms", openChatrooms);
			log.debug("{}", openChatrooms.size());
		}
		
		model.addAttribute("userNum", userNum);
		
		return "/chat/openChatMain";
	}
	
	@GetMapping(PathHandler.DMCHATWITHROOMID)
	public String dmChat(@RequestParam("dmRoomId") int dmRoomId, @AuthenticationPrincipal UserDetails user, Model model)
	{
		log.debug("go DM Chat . . .");
	    
		if (user == null || user.getUsername() == null || user.getUsername().length() <= 0)
		{
			log.debug("user의 정보를 찾을 수 없습니다.");
			return "memberView/home";
		}
		
		int userNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
		
		if (userNum <= 0)
		{
			log.debug("DB에 등록되지 않은 유저입니다");
			return "memberView/home";
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("userNum", userNum);
		params.put("dmRoomId", dmRoomId);
		
		DM_Chatroom dmChatroom = service.getDMChatroomByDMChatroomNumAnduserNum(params);
		
		if (dmChatroom == null)
		{
			log.debug("dmChatroom의 정보를 찾을 수 없습니다.");
			return "memberView/home";
		}
		
		Member myAcc = mService.memberSearchByNum(userNum);
		Member otherAcc = null;
		
		if (dmChatroom.getUser_num1() != userNum)
			otherAcc = mService.memberSearchByNum(dmChatroom.getUser_num1());
		else if (dmChatroom.getUser_num2() != userNum)
			otherAcc = mService.memberSearchByNum(dmChatroom.getUser_num2());
		
		if (myAcc == null || otherAcc == null)
		{
			log.debug("myAcc or otherAcc is NULL!");
			return "memberView/home";
		}
		
		log.debug("dmChatroom - {}", dmChatroom);
		log.debug("myAcc - {}", myAcc);
	    model.addAttribute("dmChatroom", dmChatroom);
	    model.addAttribute("myAcc", myAcc);
	    model.addAttribute("otherAcc", otherAcc);
	    return "chat/dmChat";
	}
	
	
	@MessageMapping(PathHandler.MM_LOADJOINEDCHATROOMLISTREALTIME)
	@SendTo(PathHandler.ST_LOADJOINEDCHATROOMLISTREALTIME)
	public String loadJoinedChatroomListRealTime(@DestinationVariable int chatroom_num, int userNum) throws Exception
	{
		log.debug("load Joined Chatroom List RealTime . . .");
		log.debug("chatroom : {}", chatroom_num);
		log.debug("userNum : {}", userNum);
		ArrayList<ChatroomThumbnail> chatroomThumbnails = service.getChatroomThumbnails(userNum);
		
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(chatroomThumbnails);
	}
	
	@MessageMapping(PathHandler.MM_LOADJOINEDDMCHATROOMLISTREALTIME)
	@SendTo(PathHandler.ST_lOADJOINEDDMCHATROOMLISTREALTIME)
	public String loadJoinedDMChatroomListRealTime(@DestinationVariable int DMRoomNum, int userNum)  throws Exception
	{
		log.debug("load Joined DM Chatroom List RealTime . . .");
		log.debug("DMroom_num : {}", DMRoomNum);
		log.debug("userNum : {}", userNum);
		
		ArrayList<ChatroomThumbnail> DMChatroomThumbnails = service.getDMChatroomThumbnails(userNum);
		ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(DMChatroomThumbnails);
	}
	
	@ResponseBody
	@PostMapping(PathHandler.CREATEOPENCHATROOM)
	public boolean createOpenChatroom(Chatroom chatRoom, @AuthenticationPrincipal UserDetails user)
	{
		log.debug("create open chat room . . .");
		chatRoom.setHost_num(mService.memberSearchByIdReturnUserNum(user.getUsername()));
		
		log.debug(chatRoom.toString());
		
		boolean isSuccessCreateRoom = service.createOpenChatroom(chatRoom);
		
		if (!isSuccessCreateRoom)
			log.debug("방 생성 실패!");
		
		return isSuccessCreateRoom;
	}
	
	@GetMapping(PathHandler.OPENCHATROOMENTER)
	public String openChatRoomEnter(int chatroom_num, @AuthenticationPrincipal UserDetails user, Model model)
	{
		// 여기서 현재 방 인원수를 가져와서 최대 방 인원수보다 같거나 클 경우 진입 막기.
		log.debug("move to open chat room - No.{}", chatroom_num);
		
		Chatroom chatroom = service.getChatroomById(chatroom_num);
		
		// 채팅방 유효성 검사
		if (chatroom == null)
		{
			log.debug("chatroom의 정보를 찾을 수 없습니다.");
			return "chat/openChatMain";
		}
		
		log.debug(chatroom.toString());
		
		// 채팅방에 들어온 다른 사람들 정보도 가져오기 위해 
		ArrayList<ChatroomLink> chatLinks = service.getChatroomLinksByChatroomNum(chatroom.getChatroom_num());
		log.debug("chat link - {}", chatLinks);
		
		// 채팅방 최대 인원수보다 현재 참여 인원수 이상인 경우
		if (chatroom.getCapacity() <= chatLinks.size())
		{
			log.debug("방 수용인원 초과! Chatroom num.{}", chatroom.getChatroom_num());
			return "chat/openChatMain";
		}
		
		// 내 고유번호 가져오기
		int myUserNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
		
		if (myUserNum <= 0)
		{
			log.debug("User의 고유 번호가 없습니다.");
			return "chat/openChatMain";
		}
		
		ChatroomLink chatroomLinkForSearch = new ChatroomLink();
		chatroomLinkForSearch.setUser_num(myUserNum);
		chatroomLinkForSearch.setChatroom_num(chatroom_num);
		
		ChatroomLink myChatroomLink = service.getChatroomLinkByUserNum(chatroomLinkForSearch);
		
		// 방에 처음 들어간다면 해당 방의 채팅방 링크를 DB에 저장한다.
		if (myChatroomLink == null)
		{
			myChatroomLink = new ChatroomLink();
			myChatroomLink.setChatroom_num(chatroom_num);
			myChatroomLink.setUser_num(myUserNum);
			
			int isAddedChatLink = service.createChatroomLink(myChatroomLink);
			
			if (isAddedChatLink <= 0)
			{
				log.debug("채팅방 링크를 생성 실패하였습니다.");
				return "chat/openChatMain";
			}
			
			log.debug("새 유저 입장! 채팅방 링크 생성 성공.");
			
			myChatroomLink = service.getChatroomLinkByUserNum(chatroomLinkForSearch);
		}
	
		// 해당 채팅방에서 메시지들 가져오기
		ArrayList<ChatCommon> messages = service.loadMessageByChatroomNum(chatroom.getChatroom_num());		
		
		if (messages != null)
			log.debug("total messages : {}", messages);
		else
			log.debug("message data is null!");
		
		model.addAttribute("chatroom", chatroom);
		model.addAttribute("myChatroomLink", myChatroomLink);
		model.addAttribute("chatLinks", chatLinks);
		model.addAttribute("messages", messages);
		
		log.debug("chatroom : {} , myChatroomLink : {} , chatLinks : {} , messages : {}", chatroom, myChatroomLink, chatLinks, messages);
		
		return "chat/openChatRoom";
	}
	
	// 채팅방에 입장했을때
	@MessageMapping(PathHandler.MM_OPENCHATROOMENTER)
	@SendTo(PathHandler.ST_OPENCHATROOMMESSAGESTATE)
	public String enterUser(@DestinationVariable int chatroomID, @Payload ChatCommon chat)
	{
		log.debug("enterUser . . . ");
		
		// payload 체크
		if (chat == null)
		{
			log.debug("enterUser - Payload 확인!");
			return "접속 정보를 가져오는데 실패하였습니다";
		}
		
		// 유저 이름 가져오기
		String nickName = mService.getNicknameByUserNum(chat.getUser_num());
		
		if (nickName == null || nickName == "")
		{
			log.debug("유저이름 정보를 가져오는데 실패하였습니다");
			return "유저이름 정보를 가져오는데 실패하였습니다";
		}
			
		return nickName + "님이 입장하셨습니다!";
	}
	
	@ResponseBody
	@PostMapping("allUserNickname")
	public List<Map<String, Object>> allUserNickname(int chatroom_num) {
		List<Map<String, Object>> nicknamesAndUserNums = mService.getAllUserNumsAndNicknamesByChatroomNum(chatroom_num);
		log.debug("닉네임 가져오는지 : {} ", nicknamesAndUserNums);
		return nicknamesAndUserNums;
	}
	
	@MessageMapping(PathHandler.MM_OPENCHATROOMMESSAGE)
	@SendTo(PathHandler.ST_OPENCHATROOMMESSAGE)
	public ChatCommon sendMessage(@DestinationVariable int chatroomID, @Payload ChatCommon chat)
	{
		log.info("chat : {}", chat.toString());
		
		// 채팅을 보낼때 DB에 저장된 후에 다시 돌아옴.
		ChatCommon dbChat = service.insertChatCommonAndGetChatCommon(chat);
		log.info("dbchat : {}", dbChat);
		if (dbChat == null)
		{
			log.debug("dbChat이 존재하지 않습니다!");
			return null;
		}
		
		return dbChat;
	}
	
	@MessageMapping(PathHandler.MM_DMCHATROOMMESSAGE)
	@SendTo(PathHandler.ST_DMCHATROOMMESSAGE)
	public ChatCommon dmMessage(@DestinationVariable int DMRoomNum, @Payload ChatCommon chat)
	{
		log.info("chat : {}", chat.toString());
		
		// 채팅을 보낼때 DB에 저장된 후에 다시 돌아옴.
		ChatCommon dbChat = service.insertDMChatCommonAndGetDMChatCommon(chat);
		log.info("dbchat : {}", dbChat);
		if (dbChat == null)
		{
			log.debug("dbChat이 존재하지 않습니다!");
			return null;
		}
		
		return dbChat;
	}
	
	@MessageMapping(PathHandler.MM_LOADCHATROOMNUMSBYUSERNUM)
	@SendTo(PathHandler.ST_LOADCHATROOMNUMSBYUSERNUM)
	public String loadChatRoomNumsByUserNum(@DestinationVariable int userNum) throws Exception
	{
		log.debug("load Chat Room Nums By UserNum . . .");

		ArrayList<Integer> chatroomNums =  service.loadChatRoomNumsByUserNum(userNum);

		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(chatroomNums);
	}
	
	@MessageMapping(PathHandler.MM_LOADDMCHATROOMNUMSBYUSERNUM)
	@SendTo(PathHandler.ST_LOADDMCHATROOMNUMSBYUSERNUM)
	public String loadDMChatRoomNumsByUserNum(@DestinationVariable int userNum) throws Exception
	{
		log.debug("load DM chat room nums by usernum . . .");
		
		ArrayList<Integer> DMChatroomNums = service.loadDMChatRoomNumsByUserNum(userNum);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(DMChatroomNums);
	}
	
	@ResponseBody
	@PostMapping(PathHandler.DELETEMESSAGE)
	public Boolean deleteMessage(ArrayList<ChatCommon> cc, int chatroomNum, @AuthenticationPrincipal UserDetails user)
	{
		log.debug("delete Message . . .");
		
		int userNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
	
		for (ChatCommon chatCommon : cc) 
		{
			chatCommon.setUser_num(userNum);
			chatCommon.setChatroom_num(chatroomNum);
			
			if (chatCommon.getChat_num() == 0)
			{
				log.debug("Chat_num이 필요합니다.");
				return false;
			}
		}
		
		int isDeleted = service.deleteMessage(cc);
		
		if (isDeleted <= 0)
		{
			log.debug("메시지 삭제 실패");
			return false;
		}
		
		return true;
	}
	
	// 언어 기준으로 검색시 선택한 언어만 표시될 수 있도록 오픈채팅방 목록을 가져옴.
	@GetMapping(PathHandler.SEARCHCHATROOMBYLANG)
	public String searchChatroomByLang(String lang_category, Model model
					, @RequestParam(name="page", defaultValue ="1" ) int page)
	{
		log.debug("search Chatroom By Lang . . .");
		PageNavigator navi = service.chatRoomPageNavigator(pagePerGroup, countPerPage, page);
		
		ArrayList<OpenChatroomInfo> openChatrooms = service.searchChatroomByLang(navi, lang_category);
		
		if (openChatrooms == null || openChatrooms.size() <= 0)
		{
			return "redirect:/chat/openChatMain";
		}
		
		model.addAttribute("openChatrooms", openChatrooms);
		
		log.debug("openChatrooms size - {}", openChatrooms.size());
		
		return "/chat/openChatMain";
	}
	
	// 내가 참가한 채팅방 목록들 중에 제목과 일치한 목록을 보여줌
	@MessageMapping(PathHandler.MM_SEARCHBYTITLE)
	@SendTo(PathHandler.ST_SEARCHBYTITLE)
	public String searchByTitle(@DestinationVariable int userNum, String title) throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		
		if (title == null || title.trim().isEmpty()) 
		{
	        // 검색어가 비어 있을 경우의 처리
	        return mapper.writeValueAsString("empty title");
	    }
		
		Map<String, Object> params = new HashMap<>();
	
		params.put("userNum", userNum);
		// 와일드 카드 검사를 위해 '%' 추가
		params.put("title", "%" + title + "%");

		ArrayList<ChatroomThumbnail> chatroomThumbnails = service.getChatroomThumbnailsByTitle(params);
		
		return mapper.writeValueAsString(chatroomThumbnails);
	}
	
	// 내가 참가한 DM 채팅방 목록들 중에 상대방 이름과 일치한 목록을 보여줌
	@MessageMapping(PathHandler.MM_SEARCHBYOTHERNICKNAME)
	@SendTo(PathHandler.ST_SEARCHBYOTHERNICKNAME)
	public String searchByOtherNickname(@DestinationVariable int userNum, String nickname) throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		
		if (nickname == null || nickname.trim().isEmpty()) 
		{
	        // 검색어가 비어 있을 경우의 처리
	        return mapper.writeValueAsString("empty nickname");
	    }
		
		Map<String, Object> params = new HashMap<>();
		
		params.put("userNum", userNum);
		// 와일드 카드 검사를 위해 '%' 추가
		params.put("nickname", "%" + nickname + "%");
		
		ArrayList<ChatroomThumbnail> chatroomThumbnails = service.getChatroomThumbnailsByNickname(params);
		
		return mapper.writeValueAsString(chatroomThumbnails);
	}
	
	// DM 채팅방을 만들기 전에 일회성 토큰을 먼저 발행하여 악성 유저가 주소를 아용하여 방을 계속 만들지 못하도록 방지한다. 그리고 RequestBody를 사용하여 json이 알맞은 데이터 타입으로 변환하게끔 한다.
	@PostMapping(PathHandler.GETCREDFORMAKEDMCHATROOM)
	public ResponseEntity<Map<String, String>> getCredForMakeDMChatroom(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetails user)
	{
		log.debug("get Cred For Make DM Chat room . . .");

		String nickname = request.get("nickname");
		log.debug("닉네임 출력 - {}", nickname);
		
		String token = UUID.randomUUID().toString();
		
		if (user.getUsername() == null)
		{
			log.debug("유효하지 않은 유저입니다. 로그인 바람.");
			return null;
		}
			
		int userNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
		
		if (userNum <= 0)
		{
			log.debug("DB에 유효하지 않은 유저입니다. 가입 요망.");
			return null;
		}
		
		int otherUserNum = mService.getUserIdByNickname(nickname);
		
		if (otherUserNum <= 0)
		{
			log.debug("DB에 유효하지 않은 상대 유저입니다.");
			return null;
		}
		
		Map<String, Object> params = new HashMap<>();
		
		params.put("user_num1", userNum);
		params.put("user_num2", otherUserNum);
		
		tokensUsersInfoDMChat.put(token, params);
		
	    Map<String, String> response = new HashMap<>();
	    response.put("token", token);
	    return ResponseEntity.ok(response);
	}
	
	// DM 채팅방을 만들기 전에 일회성 토큰을 먼저 발행하여 추후 다른 URL 접근시 방을 생성할 수 없도록 한다.
	@PostMapping(PathHandler.CREATEDMCHATROOM)
	public ResponseEntity<Map<String, String>> createDMChatroom(@RequestBody Map<String, String> request) 
	{
		log.debug("create DM Chat room . . .");
		
		String token = request.get("token");
		
		if (!tokensUsersInfoDMChat.containsKey(token))
		{
			log.debug("user info dm chat에 유효하지 않은 토큰입니다 - {}" , token);
			return null;
		}
		
		Map<String, Object> usersInfoDMChat = tokensUsersInfoDMChat.get(token);
		
		if (usersInfoDMChat == null)
		{
			log.debug("userinfoDMChat을 가져오지 못했습니다.");
			return null;
		}
		// 사용한 키는 삭제
		tokensUsersInfoDMChat.remove(token);
		
		String newToken = UUID.randomUUID().toString();
		DM_Chatroom dmChatroom = service.createDMChatroomAndGetNewChatroom(usersInfoDMChat);
		
		if (dmChatroom == null)
		{
			log.debug("dmChatRoom을 가져오지 못했습니다.");
			return null;
		}
		
		tokensCreateDMChat.put(newToken, dmChatroom);
		
		Map<String, String> response = new HashMap<>();
		response.put("token", newToken);
		return ResponseEntity.ok(response);
	}
	
	// 방을 만든 직후에 채팅방에서 일회성 토큰 유효성 검사를 마친 후 들어가기 위한 경로
	@ResponseBody
	@GetMapping(PathHandler.ENTERDMCHATMAINAFTERCREATEROOM)
	public Map<String, String> enterDMChatMainAfterCreateRoom(String token, Model model)
	{
		log.debug("enter DM Chat Main After Create Room . . .");
		
		Map<String, String> response = new HashMap<>();
		
		if (!tokensCreateDMChat.containsKey(token))
		{
			log.debug("create dm chat에 유효하지 않은 토큰입니다");
			response.put("redirectUrl", "/ringo/feed/feedMain");
			return response;
		}
		
		DM_Chatroom dmChatroom = tokensCreateDMChat.get(token);
		
		if (dmChatroom == null)
		{
			log.debug("dmChatroom을 가져오지 못했습니다.");
			response.put("redirectUrl", "/ringo/feed/feedMain");
			return response;
		}
		
		// 사용한 키는 삭제
		tokensCreateDMChat.remove(token);
		
		model.addAttribute("dmChatroom", dmChatroom);
		
		response.put("redirectUrl", "/ringo/chat/dmChat?dmRoomId=" + dmChatroom.getDm_chatroom_num());
		return response;
	}
	
	@GetMapping(PathHandler.CHECKEXISTENCEDMCHATROOM)
	public ResponseEntity<Boolean> checkExistenceDMChatRoom(String nickname, @AuthenticationPrincipal UserDetails user)
	{
		log.debug("check Existence DM Chat Room . . .");
		
		Map<String, Object> userNums = new HashMap<>();
		
		int userNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
		
		if (userNum <= 0)
		{
			log.debug("DB에 유효하지 않은 유저입니다. 가입 요망.");
			return ResponseEntity.ok(false);
		}
		
		int otherUserNum = mService.getUserIdByNickname(nickname);
		
		if (otherUserNum <= 0)
		{
			log.debug("DB에 유효하지 않은 상대 유저입니다.");
			return ResponseEntity.ok(false);
		}
		
		userNums.put("user_num1", userNum);
		userNums.put("user_num2", otherUserNum);
		
		DM_Chatroom dmChatroom = service.getDMChatroomByUserNums(userNums);
		
		if (dmChatroom == null)
		{
			log.debug("dmChatroom 정보를 가져올 수 없습니다.");
			return ResponseEntity.ok(false);
		}
		
		return ResponseEntity.ok(true);
	}
	
	@PostMapping(PathHandler.GETDMCHATROOMLINK)
	public ResponseEntity<Map<String, String>> getDMChatroomLink(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetails user)
	{
		String nickname = request.get("nickname");
		
		if (nickname == null)
		{
			return null;
		}
		
		log.debug("get dm chat room link . . .");
		log.debug("get dm chat room link / nickname - {}", nickname);
		
		if (user == null || user.getUsername() == null)
		{
			return null;
		}
		
		int userNum = mService.memberSearchByIdReturnUserNum(user.getUsername());

		if (userNum <= 0)
		{
			return null;
		}
		
		int otherUserNum = mService.getUserIdByNickname(nickname);
		
		if (otherUserNum <= 0)
		{
			log.debug("DB에 유효하지 않은 상대 유저입니다.");
			return null;
		}
		
		Map<String, Object> userNums = new HashMap<>();
		
		userNums.put("user_num1", userNum);
		userNums.put("user_num2", otherUserNum);
		
		DM_Chatroom dmChatroom = service.getDMChatroomByUserNums(userNums);
		
		if (dmChatroom == null)
		{
			log.debug("dmChatroom 정보를 가져올 수 없습니다.");
			return null;
		}
		
		Map<String, String> response = new HashMap<>();

		response.put("redirectUrl", "/ringo/chat/dmChat?dmRoomId=" + dmChatroom.getDm_chatroom_num());

		return ResponseEntity.ok(response);
	}
	
	@EventListener
	public void webSocketDisconnectListener(SessionDisconnectEvent event)
	{
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid 와 roomId 를 확인하여 채팅방 유저 리스트와 room에서 해당 유저를 삭제
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor : {}", headerAccessor);
	}
}