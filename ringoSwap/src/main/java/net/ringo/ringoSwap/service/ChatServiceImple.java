package net.ringo.ringoSwap.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.dao.ChatDAO;
import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;

/*
	채팅 서비스 클래스 : 여기서 사용되는 findAllRoom, createRoom,findRoomById 등은 사실상 DB와 연결되는 순간 DAO로 넘어가야 합니다.
	지금은 DB와 연결없이 만들 예정이기 때문에 우선 Service에 넣어두었습니다.
	DB 와 연결이 없기 때문에 일단 채팅방 정보가 HashMap안에 저장되어 있습니다.
	createRoom : UUID를 통해 랜덤으로 생성된 UUID값으로 채팅방 아이디를 정하고, NAME으로 채팅방 이름을 정해서 채팅방을 생성합니다.
	sendMessage : 지정된 세션에 메시지를 발송합니다.
	findOOOORoom : roomId를 기준으로 map에 담긴 채팅방 정보를 조회합니다.
*/

@Slf4j
@Data
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
	public int deleteMessage(ArrayList<ChatCommon> cc) 
	{
		return dao.deleteMessage(cc);
	}
	
	@Override
	public ArrayList<ChatroomLink> getChatroomLinks(int userNum) 
	{
		return dao.getChatroomLinks(userNum);
	}

	@Override
	public ArrayList<Chatroom> loadChatRooms(ArrayList<ChatroomLink> chatroomLinks) 
	{
		return dao.loadChatRooms(chatroomLinks);
	}

	@Override
	public ArrayList<ChatCommon> loadMessage(int chatroom_num) 
	{
		return dao.loadMessage(chatroom_num);
	}

	@Override
	public Chatroom getChatroomById(int chatroom_num) 
	{
		return dao.getChatroomById(chatroom_num);
	}

	@Override
	public ArrayList<ChatroomLink> getChatroomLinksByChatroomNum(int chatroom_num) 
	{
		return dao.getChatroomLinksByChatroomNum(chatroom_num);
	}

	@Override
	public ChatroomLink getChatroomLinkByUserNum(int userNum) 
	{
		return dao.getChatroomLinkByUserNum(userNum);
	}

	@Override
	public int insertChatCommon(ChatCommon chat) 
	{
		return dao.insertChatCommon(chat);
	}
}