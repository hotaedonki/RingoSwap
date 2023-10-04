package net.ringo.ringoSwap.dao;


import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
import net.ringo.ringoSwap.domain.DM_Chatroom;
import net.ringo.ringoSwap.domain.custom.ChatroomThumbnail;
import net.ringo.ringoSwap.domain.custom.OpenChatroomInfo;
import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.ChatLive;
import net.ringo.ringoSwap.domain.ChatMulti;

@Mapper
public interface ChatDAO 
{
	public ArrayList<Integer> getChatroomNums(int userNum);

	public ArrayList<Chatroom> getChatrooms(ArrayList<Integer> chatRoomNums);

	public int createOpenChatroom(Chatroom chatRoom);

	public int createChatroomLink(ChatroomLink chatroomLink);

	public int getMaxChatroomNum();

	public int deleteMessage(ArrayList<ChatCommon> cc);

	public ArrayList<ChatroomLink> getChatroomLinksByUserNum(int userNum);

	public ArrayList<Chatroom> loadChatRooms(ArrayList<ChatroomLink> chatroomLinks);

	public Chatroom getChatroomById(int chatroom_num);

	public ArrayList<ChatroomLink> getChatroomLinksByChatroomNum(int chatroom_num);

	public ChatroomLink getChatroomLinkByUserNum(ChatroomLink chatroomLink);

	public int insertChatCommon(ChatCommon chat);

	public ArrayList<ChatCommon> loadMessageByChatroomNum(int chatroom_num);

	// 모든 채팅방을 가져옴
	public ArrayList<Chatroom> getOpenChatrooms();

	public int getMaxChatNum();

	public ChatCommon getChatCommonByMaxChatNum(int getMaxChatNum);

	public ChatCommon getChatCommonLatest();

	public ArrayList<Chatroom> getChatroomsByChatroomLinks(ArrayList<ChatroomLink> chatroomLinks);

	public ArrayList<ChatroomThumbnail> getChatroomThumbnails(int userNum);

	public ArrayList<Integer> loadChatRoomNumsByUserNum(int userNum);

	public ArrayList<OpenChatroomInfo> searchChatroomByLang(String lang_category);

	public ArrayList<OpenChatroomInfo> getAllOpenchatrooms();

	public ArrayList<ChatroomThumbnail> getChatroomThumbnailsByTitle(Map<String, Object> params);

	public int createDMChatroom(Map<String, Object> params);

	public int getMaxDMChatroomNum();

	public DM_Chatroom getDMChatroomByDMChatroomNum(int maxDMChatroomNum);

	public DM_Chatroom getDMChatroomByUserNums(Map<String, Object> userNums);

	public DM_Chatroom getDMChatroomByDMChatroomNumAnduserNum(Map<String, Object> params);

	public ArrayList<Integer> loadDMChatRoomNumsByUserNum(int userNum);

	public ArrayList<ChatroomThumbnail> getDMChatroomThumbnails(int userNum);

	public ArrayList<ChatroomThumbnail> getChatroomThumbnailsByNickname(Map<String, Object> params);
}
