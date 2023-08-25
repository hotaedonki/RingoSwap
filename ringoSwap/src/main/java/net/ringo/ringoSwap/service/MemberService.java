package net.ringo.ringoSwap.service;

import java.util.HashMap;

import net.ringo.ringoSwap.domain.Member;

public interface MemberService 
{
	/*
	 * 회원가입 실행시 join in부문에서 작성한 회원정보를 Member vo객체 형식으로 DAO에 전송하는 메서드입니다.
	 * DB로 전송이 성공했는지를 int값으로 리턴합니다. 
	 * 0=전송 실패, 1=전송 성공 
	 */
	public int insertMember(Member m);
	
	/*
	 * 입력한 id가 중복되는 값이 있는지 계정DB를 참조하여 확인하는 메서드입니다
	 * 리턴값 = 0 : 중복되는 id값이 없습니다.
	 * 리턴값 = 1 : 중복되는 id값이 존재합니다.
	 */
	public int idCheck(String user_id);

	//자신의 id값을 매개변수로 DB에서 select문을 돌려 user_num값을 리턴받는 메서드
	public int memberSearchByIdReturnUserNum(String username);

	//멤버를 가져오는 메서드
	public Member getMember(Member member);

	public Member emailConfirmForPassword(HashMap<String, String> parameters);
}