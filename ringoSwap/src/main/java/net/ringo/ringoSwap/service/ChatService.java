package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import org.springframework.web.socket.WebSocketSession;

import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;

public interface ChatService 
{
	public ArrayList<Integer> getChatroomNums(int userNum);

	public ArrayList<Chatroom> getChatrooms(ArrayList<Integer> chatRoomNums);

	public boolean createOpenChatroom(Chatroom chatRoom);

	public int createChatroomLink(ChatroomLink chatroomLink);

	public int getMaxChatroomNum();

	public int sendMessage(ChatCommon cc);

	public int deleteMessage(ArrayList<ChatCommon> cc);

	public ArrayList<ChatroomLink> getChatroomLinks(int userNum);
	
	public ArrayList<ChatroomLink> getChatroomLinksByChatroomNum(int chatroom_num);

	public ArrayList<Chatroom> loadChatRooms(ArrayList<ChatroomLink> chatroomLinks);

	public ArrayList<ChatCommon> loadMessage(int chatroom_num);

	public void sendMessageWeb(WebSocketSession session, ChatCommon message);

	public Chatroom getChatroomById(int chatroom_num);

	public ChatroomLink getChatroomLinkByUserNum(int userNum);
}