package net.ringo.ringoSwap.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.MemberService;
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
	
	@ResponseBody
	@PostMapping(PathHandler.CREATEOPENCHATROOM)
	public void createOpenChatroom(Chatroom chatRoom, @AuthenticationPrincipal UserDetails user)
	{
		log.debug("create open chat room . . .");
		
		if (chatRoom == null)
		{
			log.debug("chatRoom is null!");
			return;
		}
		
		log.debug(chatRoom.toString());
		
		chatRoom.setHost_num(mService.memberSearchByIdReturnUserNum(user.getUsername()));
		
		int isOpenedChatroom = service.createOpenChatroom(chatRoom);
		
		if (isOpenedChatroom > 0)
		{
			log.debug("오픈 채팅방 생성 완료.");
		}
		else
		{
			log.debug("오픈 채팅방 생성 실패.");
			return;
		}
		
		ChatroomLink chatroomLink = new ChatroomLink();
		chatroomLink.setChatroom_num(isOpenedChatroom);
		
		// 링크는 반드시 채팅방이 만들어진 후에 만든다.
		int isCreatedChatroomLink = service.createChatroomLink(chatroomLink);
		
		//chatEventHandlers...() 채팅방추가 관련
		
		if (isOpenedChatroom > 0)
			log.debug("오픈 채팅방 생성 완료.");
		
	}
	
    @PostMapping("send")
    public String sendMessage(String message) 
    {
		log.debug("sendMessage Active : [ message - {} ]", message);
		
        return "redirect:/";
    }
}