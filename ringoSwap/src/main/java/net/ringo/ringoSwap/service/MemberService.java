package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;

import net.ringo.ringoSwap.domain.Member;
import net.ringo.ringoSwap.domain.MemberFollow;

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
	public int memberSearchByIdReturnUserNum(String nickname);
	

	// 이메일과 아이디가 같으면 멤버를 반환
	public Member emailConfirmForPassword(HashMap<String, String> parameters);

	public int resetPassword(Member member);
	//----------------[회원가입&로그인 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[멤버태그 기능 시작]----------------------
	//사용자가 설정한 멤버태그 배열을 member_taglink에 insert하는 테이블
	public int memberTagLinkInsertArray(String[] tagNameList, int user_num);

	//----------------[마이페이지 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[ 시작]----------------------
	//해당 id유저의 정보를 전부 가져옴
	public Member memberSearchById(String user_id);
	//해당 id유저의 정보를 전부 가져옴
	public Member memberSearchByMyPage(String user_id);
	
	//수정한 정보를 담은 member 객체를 매개변수로 보내, DB를 수정하는 메서드
	public int memberUpdateProfile(Member m);
	//수정한 계정 정보를 담은 member 객체를 매개변수로 보내, DB를 수정하는 메서드
	public int memberUpdateAccount(Member m);

	//특정 유저의 닉네임을 기반으로 검색한 값에서 회원번호만 리턴하는 메서드
	public ArrayList<Integer> memberByNicknameReturnUserNum(String nickname);
	//----------------[ 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[ 팔로우 관련 기능 시작]----------------------
	//특정 사용자의 특정 범위의 팔로워 수를 리턴하는 메서드
	public ArrayList<MemberFollow> followerArraySearch(HashMap<String, Object> map);
	//특정 사용자의 특정 범위의 팔로우 수를 리턴하는 메서드
	public ArrayList<MemberFollow> followeeArraySearch(HashMap<String, Object> map);
	//사용자가 특정 회원을 팔로우한 상태인지 확인하는 메서드
	public int followCheck(HashMap<String, Object> map);
	//사용자가 특정 회원을 팔로우 하는 메서드
	public int followInsert(int user_num, int follower_num);
	//사용자가 특정 회원을 언팔로우 하는 메서드
	public int followDelete(int user_num, int follower_num);
	//회원번호를 매개변수로 해당 회원과 친구관계인 팔로워 목록을 출력
	public ArrayList<MemberFollow> friendSelectByUserNum(int user_num);
	//----------------[ 팔로우 관련 기능 종료]----------->>>>>>>>>>>>

	//@멘션 기능을 위한 nickname으로 해당 유저의 num값을 가져오는 메서드 
	public int getUserIdByNickname(String nickname);
	//홈에 닉네임 띄우기
	public String nicknameByUserId(String userId);
	//유저 이름으로 정보를 가져오는 코드
	public Member memberSearchByUsername(String nickname);
	//유저의 고유번호로 닉네임을 가져옴
	public String getNicknameByUserNum(int user_num);
	//비밀번호 확인
	public boolean isPasswordMatching(Member member);
	//유저넘값으로 멤버 정보 가져오기
	public Member memberSearchByNum(int user_num);
}