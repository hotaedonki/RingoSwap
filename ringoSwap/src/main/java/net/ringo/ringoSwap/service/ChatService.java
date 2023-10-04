package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
import net.ringo.ringoSwap.domain.DM_Chatroom;
import net.ringo.ringoSwap.domain.custom.ChatroomThumbnail;
import net.ringo.ringoSwap.domain.custom.OpenChatroomInfo;
import net.ringo.ringoSwap.util.PageNavigator;

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
	public ArrayList<OpenChatroomInfo> searchChatroomByLang(PageNavigator navi, String lang_category);
	// 모든 오픈 채팅방 정보를 가져온다.
	public ArrayList<OpenChatroomInfo> getAllOpenchatrooms(PageNavigator navi);
	// 자신이 참가한 채팅방 목록을 제목으로 검색하여 가져온다.
	public ArrayList<ChatroomThumbnail> getChatroomThumbnailsByTitle(Map<String, Object> params);
	// DM 채팅방을 만들고 자신이 만들었던 DM 채팅방을 가져온다.
	public DM_Chatroom createDMChatroomAndGetNewChatroom(Map<String, Object> params);
	// DM 채팅방 정보를 유저 1, 2 고유 번호를 조회하여 가져온다.
	public DM_Chatroom getDMChatroomByUserNums(Map<String, Object> userNums);
	// DM 채팅방 정보를 내 유저 번호와 고유 번호로 조회하여 가져온다.
	public DM_Chatroom getDMChatroomByDMChatroomNumAnduserNum(Map<String, Object> params);
	// DM 채팅방 번호들을 유저 번호로 가져온다.
	public ArrayList<Integer> loadDMChatRoomNumsByUserNum(int userNum);
	// 자신이 참가한 DM 채팅방 목록을 가쟈온다
	public ArrayList<ChatroomThumbnail> getDMChatroomThumbnails(int userNum);
	//방 나가기
	public int leaveChatroom(Map<String, Object> params);
	//방 인원 체크
	public List<Integer> getEmptyChatrooms();

	public void deleteChatroom(int chatroomNum);
	// 자신이 참가한 DM 채팅방 중에 상대방 닉네임을 검색해서 가져온다.
	public ArrayList<ChatroomThumbnail> getChatroomThumbnailsByNickname(Map<String, Object> params);

	//네비게이터를 채팅룸에 맞게 정이하는 메서드
	public PageNavigator chatRoomPageNavigator(int pagePerGroup, int countPerPage, int page);
}