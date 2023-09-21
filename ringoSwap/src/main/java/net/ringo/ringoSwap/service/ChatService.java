package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import org.springframework.web.socket.WebSocketSession;

import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
import net.ringo.ringoSwap.domain.custom.ChatroomThumbnail;

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
}