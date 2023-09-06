package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.dao.ChatDAO;
import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;

@Slf4j
@Service
public class ChatServiceImple implements ChatService
{
	@Autowired
	private ChatDAO dao;

	@Override
	public ArrayList<Integer> getChatroomNums(int userNum) 
	{
		return dao.getChatroomNums(userNum);
	}

	@Override
	public ArrayList<Chatroom> getChatrooms(ArrayList<Integer> chatRoomNums) 
	{
		return dao.getChatrooms(chatRoomNums);
	}

	@Override
	public boolean createOpenChatroom(Chatroom chatRoom) 
	{
		// tranction 구조
		// 해당 메서드안에서 DB처리를 여러개 완료
		// = 해당 메서드가 하나의 작업공간이 된다
		int isCreatedChatroom = dao.createOpenChatroom(chatRoom);
		
		// 0 이하인 경우 채팅방 생성 실패
		if (isCreatedChatroom <= 0)
		{	
			return false;
		}
		
		// chatroom_num의 최대값을 불러옴. 최대값이 가장 최근에 만들어진(= 지금 만들어진 글)이기 때문에 이 값을 저장함
		int maxChatroomNum = dao.getMaxChatroomNum();
		
		ChatroomLink chatroomlink = new ChatroomLink();
		
		chatroomlink.setChatroom_num(maxChatroomNum);
		// 방장의 고유 번호 저장
		chatroomlink.setUser_num(chatRoom.getHost_num());
		
		int isCreatedChatroomlink = dao.createChatroomLink(chatroomlink);
		
		// 0 이하인 경우 채팅방 링크(유저가 참가한 채팅방을 표시하기 위한 링크 전용 테이블) 생성 실패
		if (isCreatedChatroomlink <= 0)
		{	
			log.debug("채팅방 링크 생성 실패");
			return false;
		}
		
		return true;
	}

	@Override
	public int createChatroomLink(ChatroomLink chatroomLink) 
	{
		return dao.createChatroomLink(chatroomLink);
	}

	@Override
	public int getMaxChatroomNum() 
	{
		return dao.getMaxChatroomNum();
	}

	@Override
	public int sendMessage(ChatCommon cc) 
	{
		return dao.sendMessage(cc);
	}

	@Override
	public int deleteMessage(ArrayList<ChatCommon> cc) 
	{
		return dao.deleteMessage(cc);
	}

	@Override
	public ArrayList<ChatroomLink> getChatroomLinks(int userNum) 
	{
		return dao.getChatroomLinks(userNum);
	}
}