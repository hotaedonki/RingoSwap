package net.ringo.ringoSwap.dao;


import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.Chatroom;
import net.ringo.ringoSwap.domain.ChatCommon;
import net.ringo.ringoSwap.domain.ChatLive;
import net.ringo.ringoSwap.domain.ChatMulti;

@Mapper
public interface ChatDAO 
{
	public ArrayList<Integer> getChatroomLinks(int userNum);

	public ArrayList<Chatroom> getChatrooms(ArrayList<Integer> chatRoomNums);
	
}