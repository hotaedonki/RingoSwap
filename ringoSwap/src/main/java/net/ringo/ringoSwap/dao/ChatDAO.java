package net.ringo.ringoSwap.dao;


import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatroomLink;
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

	public int sendMessage(ChatCommon cc);

	public int deleteMessage(ArrayList<ChatCommon> cc);

	public ArrayList<ChatroomLink> getChatroomLinks(int userNum);

	public ArrayList<Chatroom> loadChatRooms(ArrayList<ChatroomLink> chatroomLinks);

	public ArrayList<ChatCommon> loadMessage(int chatroom_num);

	public Chatroom getChatroomById(int chatroom_num);

	public ArrayList<ChatroomLink> getChatroomLinksByChatroomNum(int chatroom_num);

	public ChatroomLink getChatroomLinkByUserNum(int userNum);
}
