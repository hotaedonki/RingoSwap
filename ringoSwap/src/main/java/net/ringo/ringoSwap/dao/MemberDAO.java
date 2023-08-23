package net.ringo.ringoSwap.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.Member;

@Mapper
public interface MemberDAO 
{
	//회원가입시 회원정보를 memberDB에 insert하는 메서드
	public int insertMember(Member m);
	//중복되는 id가 있는지 매개변수 id값을 DB로 넘겨서 확인하는 메서드
	public int idCheck(String user_id);
}