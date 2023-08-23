package net.ringo.ringoSwap.service;

import java.util.HashMap;

import net.ringo.ringoSwap.domain.Member;

public interface MemberService 
{
	public int insertMember(Member m);

	
	//입력한 id가 중복되는 값이 있는지 계정DB를 참조하여 확인하는 메서드. 리턴값이 0이면 중복되는 id가 없다.
	public int idCheck(String user_id);
}