package net.ringo.ringoSwap.dao;

import java.util.ArrayList;
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
	
	//매개변수로 받은 사용자의 id로 DB에서 검색하여 나온 회원 정보를 리턴합니다.
	public Member memberSearchById(String user_id);
	
	// 이메일과 아이디가 같으면 멤버를 반환.
	public Member emailConfirmForPassword(HashMap<String, String> parameters);
	
	public int resetPassword(Member member);
	//----------------[회원가입&로그인 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[멤버태그 기능 시작]----------------------
	//태그명을 기반으로 태그 번호를 검색해 리턴하는 기능
	public ArrayList<Integer> memberTagSearchByTagNameReturnTagNum(String[] tag);
	//사용자가 설정한 멤버태그 객체가 이미 존재하는지 확인하는 메서드
	public int memberTagLinkSearch(HashMap<String, Object> map);
	//사용자가 설정한 멤버태그 객체를 DB의 taglink_member 테이블에 insert하는 메서드
	public int memberTagLinkInsert(HashMap<String, Object> map);
	//사용자의 user_num을 이용하여 현재 taglink가 걸린 tag_num을 배열로 전부 리턴받는 메서드
	public ArrayList<Integer> memberTagLinkSearchAllByUserNum(int user_num);
	//특정 taglink 객체를 DB의 taglink_member 테이블에서 delete하는 메서드
	public int memberTagLinkDelete(HashMap<String, Object> map);
	//----------------[멤버태그 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[마이페이지 기능 시작]----------------------
	//사용자의 수정한 프로필 정보 매개변수로 가져가 DB를 수정하는 메서드
	public int memberUpdateProfile(Member m);
	//사용자의 수정한 계정 정보 매개변수로 가져가 DB를 수정하는 메서드
	public int memberUpdateAccount(Member m);
}