package net.ringo.ringoSwap.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.util.FileService;
import net.ringo.ringoSwap.util.PathHandler;

@Slf4j
@Controller
@RequestMapping(PathHandler.CHAT)
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
	
	/*
	@ResponseBody
	@PostMapping(PathHandler.LOADMESSAGE)
	public ArrayList<ChatCommon> loadMessage(int chatroom_num)
	{
		ArrayList<ChatCommon> chatCommons = service.loadMessage(chatroom_num);
		
		log.debug("load Messages . . .");
	}
	*/
	
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
}