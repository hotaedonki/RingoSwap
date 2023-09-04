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
import net.ringo.ringoSwap.eventHandlers.ChatEventHandler;
import net.ringo.ringoSwap.service.ChatService;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.util.PathHandler;

@Slf4j
@Controller
@RequestMapping(PathHandler.CHAT)
public class ChatController 
{
	private final ChatEventHandler chatEventHandler;
	
    @Autowired
    public ChatController(ChatEventHandler chatEventHandler) { this.chatEventHandler = chatEventHandler; }
	
	@Autowired
	ChatService service;
	
	@Autowired
	MemberService mService;
	
	//채팅서비스의 메인페이지로 이동하는 컨트롤러 메서드
	@GetMapping(PathHandler.CHATMAIN)
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
		
		return "chat/chatMain";
	}
	
    @PostMapping("send")
    public String sendMessage(String message) 
    {
		log.debug("sendMessage Active : [ message - {} ]", message);
		
        // Socket.IO 이벤트 핸들러 호출하여 통신
		chatEventHandler.handleSendMessageEvent(message);
        return "redirect:/";
    }
}