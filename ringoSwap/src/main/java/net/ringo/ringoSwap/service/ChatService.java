package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
import net.ringo.ringoSwap.domain.DM_Chatroom;
import net.ringo.ringoSwap.domain.custom.ChatroomThumbnail;
import net.ringo.ringoSwap.domain.custom.OpenChatroomInfo;

public interface ChatService 
{
	public ArrayList<Integer> getChatroomNums(int userNum);

	public ArrayList<Chatroom> getChatrooms(ArrayList<Integer> chatRoomNums);

	public boolean createOpenChatroom(Chatroom chatRoom);

	public int createChatroomLink(ChatroomLink chatroomLink);

	public int getMaxChatroomNum();
	
	public int getMaxChatNum();

	public int deleteMessage(ArrayList<ChatCommon> cc);

	public ArrayList<ChatroomLink> getChatroomLinksByUserNum(int userNum);
	
	public ArrayList<ChatroomLink> getChatroomLinksByChatroomNum(int chatroom_num);

	public ArrayList<Chatroom> loadChatRooms(ArrayList<ChatroomLink> chatroomLinks);

	public Chatroom getChatroomById(int chatroom_num);

	public ChatroomLink getChatroomLinkByUserNum(ChatroomLink chatroomLink);

	public int insertChatCommon(ChatCommon chat);

	public ArrayList<ChatCommon> loadMessageByChatroomNum(int chatroom_num);

	// 채팅방 리스트 가져오기
	public ArrayList<Chatroom> getOpenChatrooms();

	// ChatCommon 저장 후, 성공적으로 저장하면 다시 채팅방에 전달하기 위해 ChatCommon을 반환
	public ChatCommon insertChatCommonAndGetChatCommon(ChatCommon chat);

	public ArrayList<ChatroomThumbnail> getChatroomThumbnails(int userNum);

	public ArrayList<Integer> loadChatRoomNumsByUserNum(int userNum);

	// 오픈 채팅방 정보들을 언어 필터 기준으로 가져온다.
	public ArrayList<OpenChatroomInfo> searchChatroomByLang(String lang_category);
	// 모든 오픈 채팅방 정보를 가져온다.
	public ArrayList<OpenChatroomInfo> getAllOpenchatrooms();
	// 자신이 참가한 채팅방 목록을 제목으로 검색하여 가져온다.
	public ArrayList<ChatroomThumbnail> getChatroomThumbnailsByTitle(Map<String, Object> params);
	// DM 채팅방을 만들고 자신이 만들었던 DM 채팅방을 가져온다.
	public DM_Chatroom createDMChatroomAndGetNewChatroom(Map<String, Object> params);
	// DM 채팅방 정보를 유저 1, 2 고유 번호를 조회하여 가져온다.
	public DM_Chatroom getDMChatroomByUserNums(Map<String, Object> userNums);
}