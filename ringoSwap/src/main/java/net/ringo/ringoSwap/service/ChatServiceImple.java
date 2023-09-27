package net.ringo.ringoSwap.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.dao.ChatDAO;
import net.ringo.ringoSwap.dao.MemberDAO;
import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
import net.ringo.ringoSwap.domain.DM_Chatroom;
import net.ringo.ringoSwap.domain.custom.ChatroomThumbnail;
import net.ringo.ringoSwap.domain.custom.OpenChatroomInfo;
import net.ringo.ringoSwap.enums.webService.MessageType;

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
	
	@Autowired
	private MemberDAO mDao;
	
	
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

	// 채팅방 링크를 DB에 저장한다.
	@Override
	public int createChatroomLink(ChatroomLink chatroomLink) 
	{
		return dao.createChatroomLink(chatroomLink);
	}
	
	// 채팅방의 nextval 최대값을 가져온다.
	@Override
	public int getMaxChatroomNum() 
	{
		return dao.getMaxChatroomNum();
	}

	// 메세지를 지운다
	@Override
	public int deleteMessage(ArrayList<ChatCommon> cc) 
	{
		return dao.deleteMessage(cc);
	}
	
	// 채팅방 링크를 해당 유저 기준으로 가져온다.
	@Override
	public ArrayList<ChatroomLink> getChatroomLinksByUserNum(int userNum) 
	{
		return dao.getChatroomLinksByUserNum(userNum);
	}

	// 채팅방들을 채팅방 링크들 기준을 가져온다.
	@Override
	public ArrayList<Chatroom> loadChatRooms(ArrayList<ChatroomLink> chatroomLinks) 
	{
		return dao.loadChatRooms(chatroomLinks);
	}

	// 채팅방을 채팅 고유 번호 기준으로 가져온다.
	@Override
	public Chatroom getChatroomById(int chatroom_num) 
	{
		return dao.getChatroomById(chatroom_num);
	}

	// 채팅방 링크들을 채팅방 고유 번호 기준을 가져온다.
	@Override
	public ArrayList<ChatroomLink> getChatroomLinksByChatroomNum(int chatroom_num) 
	{
		return dao.getChatroomLinksByChatroomNum(chatroom_num);
	}
	
	// 해당 유저의 채팅방 링크를 가져온다.
	@Override
	public ChatroomLink getChatroomLinkByUserNum(ChatroomLink chatroomLink) 
	{
		return dao.getChatroomLinkByUserNum(chatroomLink);
	}
	
	// 채팅 메시지(ChatCommon)를 저장한다.
	@Override
	public int insertChatCommon(ChatCommon chat) 
	{
		return dao.insertChatCommon(chat);
	}

	// 해당 채팅방 번호 기준을 메세지들을 가져온다.
	@Override
	public ArrayList<ChatCommon> loadMessageByChatroomNum(int chatroom_num) {
		// TODO Auto-generated method stub
		return dao.loadMessageByChatroomNum(chatroom_num);
	}

	// 현재 열려있는 모든 채팅방을 가져온다.
	@Override
	public ArrayList<Chatroom> getOpenChatrooms() 
	{
		return dao.getOpenChatrooms();
	}

	@Override
	public ChatCommon insertChatCommonAndGetChatCommon(ChatCommon chat) 
	{
        int isSuccessedSaveChat = dao.insertChatCommon(chat);
        
        if (isSuccessedSaveChat <= 0)
        {
        	log.debug("DB에 ChatCommon 저장 실패");
        	return null;
        }
        
        ChatCommon dbChat = dao.getChatCommonLatest();
        
        if (dbChat == null)
        {
        	log.debug("dbChat 불러오기 실패");
        	return null;
        }
        
        dbChat.setType(MessageType.TALK);
        
        return dbChat;
	}

	@Override
	public int getMaxChatNum() 
	{
		return dao.getMaxChatNum();
	}

	// 채팅방 메인에서 채팅방 목록들에 대한 정보들을 가져오기 위한 기능
	@Override
	public ArrayList<ChatroomThumbnail> getChatroomThumbnails(int userNum) 
	{
		/* 필요한 정보 : 채팅방 고유 번호, 채팅방 제목, 가장 마지막으로 입력된 메시지 입력 날짜, 가장 마지막으로 입력된 메시지 
		 * int chatroom_num; String title; String inputdate; String message;
		 */
		
		return dao.getChatroomThumbnails(userNum);
	}

	// 유저가 속해있는 채팅방의 고유 번호들을 유저 고유 번호로 가져온다.
	@Override
	public ArrayList<Integer> loadChatRoomNumsByUserNum(int userNum) 
	{
		return dao.loadChatRoomNumsByUserNum(userNum);
	}

	// 오픈 채팅방 정보를 언어 필터 기준으로 가져온다.
	@Override
	public ArrayList<OpenChatroomInfo> searchChatroomByLang(String lang_category) 
	{
		return dao.searchChatroomByLang(lang_category);
	}

	@Override
	public ArrayList<OpenChatroomInfo> getAllOpenchatrooms() 
	{
		// TODO Auto-generated method stub
		return dao.getAllOpenchatrooms();
	}

	@Override
	public ArrayList<ChatroomThumbnail> getChatroomThumbnailsByTitle(Map<String, Object> params) 
	{
		return dao.getChatroomThumbnailsByTitle(params);
	}

	@Override
	public DM_Chatroom createDMChatroomAndGetNewChatroom(Map<String, Object> params) 
	{
		int isCreatedDMChatroom =  dao.createDMChatroom(params);
		
		// 0 이하인 경우엔 DM 채팅방 생성 실패
		if (isCreatedDMChatroom <= 0)
		{
			return null;
		}
		
		// dm_chatroom_num의 최대값을 불러옴. 최대값이 가장 최근에 만들어진(= 지금 만들어진 글)이기 때문에 이 값을 저장함
		int maxDMChatroomNum = dao.getMaxDMChatroomNum();
		
		DM_Chatroom dmChatroom = dao.getDMChatroomByDMChatroomNum(maxDMChatroomNum);
		
		return dmChatroom;
	}

	@Override
	public DM_Chatroom getDMChatroomByUserNums(Map<String, Object> userNums) 
	{
		return dao.getDMChatroomByUserNums(userNums);
	}
}