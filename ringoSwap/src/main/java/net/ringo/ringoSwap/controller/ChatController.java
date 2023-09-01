package net.ringo.ringoSwap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
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
    public ChatController(ChatEventHandler chatEventHandler) 
    {
        this.chatEventHandler = chatEventHandler;
    }
	
	@Autowired
	ChatService service;
	
	//채팅서비스의 메인페이지로 이동하는 컨트롤러 메서드
	@GetMapping(PathHandler.CHATMAIN)
	public String chatMain()
	{
		return "chat/chatMain";
	}
	
	@ResponseBody
    @PostMapping("send")
    public String sendMessage(String message) 
    {
        // Socket.IO 이벤트 핸들러 호출하여 통신
		chatEventHandler.handleSendMessageEvent(message);
        return "redirect:/";
    }
}