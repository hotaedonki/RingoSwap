package net.ringo.ringoSwap.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ringo.ringoSwap.dao.ChatDAO;
import net.ringo.ringoSwap.domain.Chatroom;


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
}
