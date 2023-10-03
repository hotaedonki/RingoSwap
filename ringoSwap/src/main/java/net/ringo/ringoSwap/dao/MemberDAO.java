package net.ringo.ringoSwap.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.Member;
import net.ringo.ringoSwap.domain.MemberFollow;

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
	
	//존재하는 이메일인지 체크
	public int emailCheck(String email);
	
	//존재하는 닉네임인지 체크
	public int nicknameCheck(String nickname);
	
	//이메일로 아이디 찾아오기
	public String userIDByEmail(String email);
	
	//현재 로그인한 유저의 프로필사진 가져오기
	public String printMyProfilePhoto(String user_id);
	
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
	//회원번호를 매개변수로 해당 회원의 회원태그 목록을 출력하는 메서드
	public ArrayList<String> memberTagSelectByUserNum(int user_num);
	//----------------[멤버태그 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[마이페이지 기능 시작]----------------------
	//사용자의 수정한 프로필 정보 매개변수로 가져가 DB를 수정하는 메서드
	public int memberUpdateProfile(Member m);
	//사용자의 수정한 계정 정보 매개변수로 가져가 DB를 수정하는 메서드
	public int memberUpdateAccount(Member m);
	//사용자의 개인정보 수정 정보를 매개변수로 DB에 전달하여 member테이블을 수정하는 메서드
	public int memberUpdatePersonalInfo(HashMap<String, Object> map);
	//회원의 닉네임을 기반으로 해당 문자열이 포함된 닉네임을 가진 모든 사용자의 회원번호를 리턴하는 메서드.
	public ArrayList<Integer> memberByNicknameReturnUserNum(String nickname);
	//특정 사용자의 특정 범위의 팔로워 회원 목록을 리턴하는 메서드
	public ArrayList<MemberFollow> followerArraySearch(HashMap<String, Object> map);
	//특정 사용자의 특정 범위의 팔로우 회원 목록을 리턴하는 메서드
	public ArrayList<MemberFollow> followeeArraySearch(HashMap<String, Object> map);
	//사용자가 특정 회원을 팔로우하는 메서드
	public int followInsert(HashMap<String, Object> map);
	//해당 팔로우를 삭제해 언팔로우하는 메서드
	public int followDelete(HashMap<String, Object> map);
	//특정 회원이 다른 회원을 팔로우 했는지 확인하는 메서드
	public int followSearch(HashMap<String, Object> map);
	//특정 회원이 다른 회원과 친구상태인지 확인하는 메서드
	public int followSearchReturnFriendCheck(HashMap<String, Object> map);
	//친구상태로 변경하는 메서드
	public int followFriendUpdate(HashMap<String, Object> map);
	//친구상태를 해제하는 메서드
	public int followFriendRelease(HashMap<String, Object> map);
	//회원번호를 매개변수로 해당 회원과 친구관계인 회원목록을 리턴하는 메서드
	public ArrayList<MemberFollow> friendSelectByUserNum(int user_num);
	//@멘션 기능을 위한 nickname으로 해당 유저의 num값을 가져오는 메서드 
	public int getUserIdByNickname(String nickname);
	//유저 아이디로 닉네임값을 가져옴	
	public String nicknameByUserId(String userId);
	// 유저 고유 번호로 닉네임을 가져온다.
	public Member memberSearchByUsername(String nickname);
	public String getNicknameByUserNum(int user_num);
	// 비밀번호 확인
	public String getEncodedPasswordByUserNum(int user_num);
	// 유저넘으로 멤버값 가져오기
	public Member memberSearchByNum(int user_num);
	public List<Map<String, Object>> getAllUserNumsAndNicknamesByChatroomNum(int chatroom_num);

	// 회원가입시 생성된 회원번호를 기반으로 ringo_game_setting테이블에 insert하는 메서드
	public int memberGameSettingInsert(int user_num);
}