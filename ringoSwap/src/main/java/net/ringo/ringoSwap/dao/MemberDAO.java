package net.ringo.ringoSwap.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.Member;

@Mapper
public interface MemberDAO 
{
	//회원가입시 회원정보를 memberDB에 insert하는 메서드
	public int insertMember(Member m);
	//회원가입시 email과 인증코드 확인하여 실제로 되었는지 확인하는 코드
	public int emailCheck(HashMap<String, Object> map);
	//로그인을 위한 로그인 메서드
	public Member memberLogin(Member m);
}