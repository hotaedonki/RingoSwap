package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import net.ringo.ringoSwap.domain.Chatroom;

public interface ChatService 
{
	public ArrayList<Integer> getChatroomLinks(int userNum);

	public ArrayList<Chatroom> getChatrooms(ArrayList<Integer> chatRoomNums);

}
