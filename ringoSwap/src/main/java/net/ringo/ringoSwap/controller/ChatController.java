package net.ringo.ringoSwap.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.util.PathHandler;

@Slf4j
@Controller
@RequestMapping(PathHandler.CHAT)
@RequiredArgsConstructor
public class ChatController 
{	
	private final SimpMessageSendingOperations template;
	
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
		
		// 가져온 user_num으로 chat room의 고유 번호들을 가져온다.
		ArrayList<Integer> chatRoomNums = service.getChatroomNums(userNum);
		
		log.debug("{}", chatRoomNums.size());
		
		// chatRoomNums이 있는 경우(= 한개 이상 링크(ringo_chatroom_link)가 있는 경우) chat room의 정보를 가져온다.
		if (chatRoomNums != null && chatRoomNums.size() > 0)
		{
			ArrayList<Chatroom> chatrooms = service.getChatrooms(chatRoomNums);
			model.addAttribute("chatrooms", chatrooms);
		}
		
		return "chat/openChatMain";
	}
	
	@GetMapping(PathHandler.CREATEROOMPAGE)
	public String createRoomPage()
	{
		return "chat/createRoom";
	}
	
	@PostMapping(PathHandler.CREATEOPENCHATROOM)
	public String createOpenChatroom(Chatroom chatRoom, @AuthenticationPrincipal UserDetails user)
	{
	
		log.debug("create open chat room . . .");
		chatRoom.setHost_num(mService.memberSearchByIdReturnUserNum(user.getUsername()));
		
		log.debug(chatRoom.toString());
		
		boolean isSuccessCreateRoom = service.createOpenChatroom(chatRoom);

		//chatEventHandlers...() 채팅방 서버 기능 관련 함수 추가하기
		return "redirect:/";
	}
	
	/*	나중엔 Ajax 형태로 받아오도록 하기.
	@ResponseBody
	@PostMapping(PathHandler.CREATEOPENCHATROOM)
	public boolean createOpenChatroom(Chatroom chatRoom, @AuthenticationPrincipal UserDetails user)
	{
		log.debug("create open chat room . . .");
		
		if (chatRoom == null)
		{
			log.debug("chatRoom is null!");
			return false;
		}
		
		log.debug(chatRoom.toString());
		
		chatRoom.setHost_num(mService.memberSearchByIdReturnUserNum(user.getUsername()));
		
		boolean isSuccessCreateRoom = service.createOpenChatroom(chatRoom);
		
		return isSuccessCreateRoom;
		//chatEventHandlers...() 채팅방 서버 기능 관련 함수 추가하기
	}
	*/
	
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
	
	@ResponseBody
	@PostMapping(PathHandler.LOADMESSAGE)
	public ArrayList<ChatCommon> loadMessage(int chatroom_num)
	{
		ArrayList<ChatCommon> chatCommons = service.loadMessage(chatroom_num);
		
		log.debug("load Messages . . .");
		
		return chatCommons;
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
		
		ChatroomLink myChatroomLink = service.getChatroomLinkByUserNum(myUserNum);
		
		// 방에 처음 들어간다면 해당 방의 채팅방 링크를 DB에 저장한다.
		if (myChatroomLink == null)
		{
			myChatroomLink.setChatroom_num(chatroom_num);
			myChatroomLink.setUser_num(myUserNum);
			
			int isAddedChatLink = service.createChatroomLink(myChatroomLink);
			
			if (isAddedChatLink <= 0)
			{
				log.debug("채팅방 링크를 생성 실패하였습니다.");
				return "redirect:/";
			}
			
			log.debug("새 유저 입장! 채팅방 링크 생성 성공.");
		}
		
		// 채팅방에 들어온 다른 사람들 정보도 확인하기 위해 링크들을 가져옴
		ArrayList<ChatroomLink> chatLinks = service.getChatroomLinksByChatroomNum(chatroom.getChatroom_num());
		log.debug("chat link size - {}", chatLinks.size());
		
		
		// 해당 채팅방에서 메시지들 가져오기
		ArrayList<ChatCommon> messages = service.loadMessage(chatroom.getChatroom_num());		
		
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
	
	/*
	@ResponseBody
    @PostMapping(PathHandler.SENDMESSAGE)
    public Boolean sendMessage(ChatCommon cc, @AuthenticationPrincipal UserDetails user, MultipartFile upload) 
    {
		log.debug("send Message . . .");
		
    	// 채팅방의 고유 번호가 없는 경우 메시지를 보낼 수 없다.
    	if (cc.getChatroom_num() <= 0)
    	{
    		log.debug("채팅방의 고유 번호가 필요합니다.");
    		return false;
    	}
    	
    	// 채팅 메시지 값이 없으면 메시지를 보낼 수 없다.
    	if (cc.getMessage() == null || cc.getMessage().length() <= 0)
    	{
    		log.debug("채팅방 메시지가 비어있습니다.");
    		return false;
    	}
    	
    	cc.setUser_num(mService.memberSearchByIdReturnUserNum(user.getUsername()));
    	
    	if (upload != null && !upload.isEmpty())	// 사진이 있을때
    	{
    		String savedFile = FileService.saveFile(upload, uploadPath);
    		cc.setOrigin_file(upload.getOriginalFilename());
    		cc.setSaved_file(savedFile);
    	}
		
    	log.debug("{}", cc.toString());
    	
    	int isSended = service.sendMessage(cc);
    	
    	if (isSended <= 0)
    	{
    		log.debug("메시지 정보 DB에 삽입 실패");
    		return false;
    	}
		
        return true;
    }
    */
	
	/*
	@MessageMapping(PathHandler.MM_OPENCHATROOMENTER)
	public void enterUser(@Payload ChatCommon chat, SimpMessageHeaderAccessor headerccessor)
	{
		chat.setMessage(chat.getChat_num() + "님이 입장하셨습니다.");
		template.convertAndSend("/sub/chat/openChatroom" + chat.getChatroom_num(), chat);
	}
	*/
	
	@MessageMapping("/chat/openChatRoomEnter/")
	public void enterUser(@Payload ChatCommon chat, SimpMessageHeaderAccessor headerccessor)
	{
		chat.setMessage(chat.getChat_num() + "님이 입장하셨습니다.");
		template.convertAndSend("/sub/chat/openChatroom" + chat.getChatroom_num(), chat);
	}
	
	@MessageMapping(PathHandler.MM_SENDMESSAGE)
	public void sendMessage(@Payload ChatCommon chat)
	{
		log.info("chat : {}", chat.toString());
		chat.setMessage(chat.getMessage());
		template.convertAndSend("/sub/chat/openChatroom" + chat.getChatroom_num(), chat);;
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
        
        /*
        // 채팅방 유저 -1
        repository.decreaseUser(roomId);

        //채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String userName = repository.getUserName(roomId, userUUID);
        repository.deleteUser(roomId,userUUID);

        if(userName != null){
            log.info("User Disconnected : " + userName);

            ChatDto chat = ChatDto.builder()
                    .type(ChatDto.MessageType.LEAVE)
                    .sender(userName)
                    .message(userName + "님이 퇴장하였습니다.")
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId,chat);
            */
	}
	
	/*
	 // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/chat/uselist")
    @ResponseBody
    public List<String> userList(String roomId){

        return repository.getUserList(roomId);
    }
    */

	/*
    // 채팅에 참여한 유저 닉네임 중복 확인
    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId")String roomId ,
                                  @RequestParam("username")String username){

        String userName = repository.isDuplicateName(roomId, username);
        log.info("DuplicateName : {}", userName);

        return userName;
    }
    */
}