package net.ringo.ringoSwap.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.Member;

@Mapper
public interface MemberDAO 
{
	/*
	 * service에서 인계받은 회원정보를 Member vo객체 형식으로 xml파일을 통해 DB에 전송하는 메서드입니다.
	 */
	public int insertMember(Member m);
	/*
	 * 입력한 id가 중복되는 값이 있는지 계정DB를 참조하여 확인하는 메서드입니다
	 * 리턴값 = 0 : 중복되는 id값이 없습니다.
	 * 리턴값 = 1 : 중복되는 id값이 존재합니다.
	 */
	public int idCheck(String user_id);
}