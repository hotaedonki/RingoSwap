package net.ringo.ringoSwap.controller;

import java.util.ArrayList;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
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
	public String chatMain(Model model, @AuthenticationPrincipal UserDetails user)
	{
		log.debug("move to chat/chatMain . . .");
		
		// user의 이름으로 user_num을 가져온다.
		int userNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
		
		ArrayList<Chatroom> chatrooms = service.getOpenChatrooms();
		
		log.debug("{}", chatrooms.size());
		
		// chatRoomNums이 있는 경우(= 한개 이상 링크(ringo_chatroom_link)가 있는 경우) chat room의 정보를 가져온다.
		if (chatrooms != null && chatrooms.size() > 0)
		{
			model.addAttribute("chatrooms", chatrooms);
		}
		
		return "chat/openChatMain";
	}
	
	@ResponseBody
	@PostMapping(PathHandler.CREATEOPENCHATROOM)
	public boolean createOpenChatroom(Chatroom chatRoom, @AuthenticationPrincipal UserDetails user)
	{
	
		log.debug("create open chat room . . .");
		chatRoom.setHost_num(mService.memberSearchByIdReturnUserNum(user.getUsername()));
		
		log.debug(chatRoom.toString());
		
		boolean isSuccessCreateRoom = service.createOpenChatroom(chatRoom);

		//chatEventHandlers...() 채팅방 서버 기능 관련 함수 추가하기
		
		if (!isSuccessCreateRoom)
			log.debug("방 생성 실패!");
		
		return isSuccessCreateRoom;
	}
	
	@ResponseBody
	@PostMapping(PathHandler.LOADCHATROOMS)
	public ArrayList<Chatroom> loadChatRooms(@AuthenticationPrincipal UserDetails user)
	{
		int userNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
		ArrayList<ChatroomLink> chatroomLinks = service.getChatroomLinks(userNum);
		
		if (chatroomLinks == null)
			return null;
		
		ArrayList<Chatroom> chatrooms = service.loadChatRooms(chatroomLinks);
		
		if (chatrooms == null || chatrooms.size() <= 0)
		{
			return null;
		}
		
		return chatrooms;
	}
	
	/*
	@MessageMapping(PathHandler.MM_OPENCHATROOMENTER)
	@SendTo(PathHandler.ST_OPENCHATROOMMESSAGESTATE)
	public ArrayList<Chatroom> loadChatRoomsRealTime(@AuthenticationPrincipal UserDetails user)
	{
		int userNum = mService.memberSearchByIdReturnUserNum(user.getUsername());
		ArrayList<ChatroomLink> chatroomLinks = service.getChatroomLinks(userNum);
		
		if (chatroomLinks == null)
			return null;
		
		ArrayList<Chatroom> chatrooms = service.loadChatRooms(chatroomLinks);
		
		if (chatrooms == null || chatrooms.size() <= 0)
		{
			return null;
		}
		
		return chatrooms;
	}
	*/
	
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
		int myUserNum = mService.memberSearchById(user.getUsername()).getUser_num();
		
		if (myUserNum <= 0)
		{
			log.debug("User의 고유 번호가 없습니다.");
			return "redirect:/";
		}
		
		ChatroomLink myChatroomLink = service.getChatroomLinkByUserNum(myUserNum);
		
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
			
			myChatroomLink = service.getChatroomLinkByUserNum(myUserNum);
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
		String nickName = mService.getUsernameByUserNum(chat.getUser_num());
		
		if (nickName == null || nickName == "")
		{
			log.debug("유저이름 정보를 가져오는데 실패하였습니다");
			return "유저이름 정보를 가져오는데 실패하였습니다";
		}
		
		// 채팅방 링크가 존재하는지 확인하기 위해 가져옴
		ChatroomLink isExistChatroomlink = service.getChatroomLinkByUserNum(chat.getUser_num());
		
		// 없으면 새로운 링크를 만듬
		if (isExistChatroomlink == null)
		{
			ChatroomLink newChatroomLink = new ChatroomLink();
			newChatroomLink.setChatroom_num(chat.getChatroom_num());
			newChatroomLink.setUser_num(chat.getUser_num());
			
			// 채팅방 링크를 성공적으로 만들었는가?
			int isSuccessCreateChatroomLink = service.createChatroomLink(newChatroomLink);
			
			if (isSuccessCreateChatroomLink <= 0)
			{
				log.debug("채팅룸 링크 생성 실패!");
				return "채팅룸 링크 생성 실패!";
			}
		}
		
		return nickName + "님이 입장하셨습니다!";
	}
	
	@MessageMapping(PathHandler.MM_OPENCHATROOMMESSAGE)
	@SendTo(PathHandler.ST_OPENCHATROOMMESSAGE)
	public ChatCommon sendMessage(@DestinationVariable int chatroomID, @Payload ChatCommon chat)
	{
		log.info("chat : {}", chat.toString());
		
		ChatCommon dbChat = service.insertChatCommonAndGetChatCommon(chat);
		
		if (dbChat == null)
		{
			log.debug("dbChat이 존재하지 않습니다!");
			return null;
		}
		
		return dbChat;
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
	
	@EventListener
	public void webSocketDisconnectListener(SessionDisconnectEvent event)
	{
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid 와 roomId 를 확인하여 채팅방 유저 리스트와 room에서 해당 유저를 삭제
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor : {}",headerAccessor);
	}
}