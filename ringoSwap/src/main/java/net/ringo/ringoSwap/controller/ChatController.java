package net.ringo.ringoSwap.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
import net.ringo.ringoSwap.domain.custom.ChatroomThumbnail;
import net.ringo.ringoSwap.domain.custom.OpenChatroomInfo;
import net.ringo.ringoSwap.enums.webService.MessageType;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.MemberService;
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
	
	//채팅서비스의 메인페이지로 이동하는 컨트롤러 메서드
	@GetMapping(PathHandler.OPENCHATMAIN)
	public String chatMain(String lang_category, Model model, @AuthenticationPrincipal UserDetails user)
	{
		log.debug("move to chat/openChatMain . . .");
		
		// user의 이름으로 user_num을 가져온다.
		int userNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
		
		ArrayList<OpenChatroomInfo> openChatrooms = new ArrayList<>();
		
		// 언어 필터 관련해서 값이 있으면
		if (lang_category != null && (lang_category.equals("ko") || lang_category.equals("en") || lang_category.equals("ja")))
			openChatrooms = service.searchChatroomByLang(lang_category);
		else
			openChatrooms = service.getAllOpenchatrooms();
		
		// chatRoomNums이 있는 경우(= 한개 이상 링크(ringo_chatroom_link)가 있는 경우) chat room의 정보를 가져온다.
		if (openChatrooms != null && openChatrooms.size() > 0)
		{
			model.addAttribute("openChatrooms", openChatrooms);
			log.debug("{}", openChatrooms.size());
		}
		
		model.addAttribute("userNum", userNum);
		
		return "/chat/openChatMain";
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
		log.debug("move to open chat room - No.{}", chatroom_num);
		
		Chatroom chatroom = service.getChatroomById(chatroom_num);
		
		if (chatroom == null)
		{
			log.debug("chatroom의 정보를 찾을 수 없습니다.");
			return "redirect:/";
		}
		
		log.debug(chatroom.toString());
		
		// 내 고유번호 가져오기
		int myUserNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
		
		if (myUserNum <= 0)
		{
			log.debug("User의 고유 번호가 없습니다.");
			return "redirect:/";
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
				return "redirect:/";
			}
			
			log.debug("새 유저 입장! 채팅방 링크 생성 성공.");
			
			myChatroomLink = service.getChatroomLinkByUserNum(chatroomLinkForSearch);
		}
		
		// 채팅방에 들어온 다른 사람들 정보도 확인하기 위해 링크들을 가져옴
		ArrayList<ChatroomLink> chatLinks = service.getChatroomLinksByChatroomNum(chatroom.getChatroom_num());
		log.debug("chat link size - {}", chatLinks.size());
		
		
		// 해당 채팅방에서 메시지들 가져오기
		ArrayList<ChatCommon> messages = service.loadMessageByChatroomNum(chatroom.getChatroom_num());		
		
		if (messages != null)
			log.debug("total messages : {}", messages.size());
		else
			log.debug("message data is null!");
		
		model.addAttribute("chatroom", chatroom);
		model.addAttribute("myChatroomLink", myChatroomLink);
		model.addAttribute("chatLinks", chatLinks);
		model.addAttribute("messages", messages);
		
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
	
	@MessageMapping(PathHandler.MM_OPENCHATROOMMESSAGE)
	@SendTo(PathHandler.ST_OPENCHATROOMMESSAGE)
	public ChatCommon sendMessage(@DestinationVariable int chatroomID, @Payload ChatCommon chat)
	{
		log.info("chat : {}", chat.toString());
		
		// 채팅을 보낼때 DB에 저장된 후에 다시 돌아옴.
		ChatCommon dbChat = service.insertChatCommonAndGetChatCommon(chat);
		
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
	public String searchChatroomByLang(String lang_category, Model model)
	{
		log.debug("search Chatroom By Lang . . .");
		
		ArrayList<OpenChatroomInfo> openChatrooms = service.searchChatroomByLang(lang_category);
		
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
		
		Map<Integer, String> params = new HashMap();
	
		
		ArrayList<ChatroomThumbnail> chatroomThumbnails = service.getChatroomThumbnailsByTitle(title);
		
		return mapper.writeValueAsString(chatroomThumbnails);
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