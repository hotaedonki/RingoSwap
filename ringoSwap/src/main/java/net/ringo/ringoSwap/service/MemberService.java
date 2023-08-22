package net.ringo.ringoSwap.service;

import java.util.HashMap;

import net.ringo.ringoSwap.domain.Member;

public interface MemberService 
{
	public int insertMember(Member m);

	//로그인을 위해 id와 PW를 DB로 전송하여 해당 계정이 있는지를 select문으로 확인하는 메서드
	public Member memberLogin(Member m);
}