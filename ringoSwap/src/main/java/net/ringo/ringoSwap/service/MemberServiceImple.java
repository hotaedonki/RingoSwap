package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.dao.MemberDAO;
import net.ringo.ringoSwap.domain.Member;
import net.ringo.ringoSwap.domain.MemberFollow;

@Slf4j
@Service
public class MemberServiceImple implements MemberService 
{
	@Autowired
	private MemberDAO dao;
	//비밀번호 변수인 password를 암호화하는 메서드를 호출하는 Autowired입니다.
	@Autowired
	private PasswordEncoder passwordEncoder;

	/*
	 * 회원가입 실행시 join in부문에서 작성한 회원정보를 Member vo객체 형식으로 DAO에 전송하는 메서드입니다.
	 */
	@Override
	public int insertMember(Member m) 
	{
		m.setPassword(passwordEncoder.encode(m.getPassword()));
		dao.insertMember(m);
		int user_num = m.getUser_num();
		log.debug("생성 번호 {}", user_num);
		return dao.memberGameSettingInsert(user_num);
	}
	/*
	 * 입력한 id가 중복되는 값이 있는지 계정DB를 참조하여 확인하는 메서드입니다
	 */
	@Override
	public int idCheck(String user_id) {
		return dao.idCheck(user_id);
	}
	//
	@Override
	public int memberSearchByIdReturnUserNum(String user_id) {
		Member member = dao.memberSearchById(user_id);
		return member.getUser_num();
	}
	
	@Override
	public int emailCheck(String email) {
		return dao.emailCheck(email);
	}
	
	@Override
	public int nicknameCheck(String nickname) {
		return dao.nicknameCheck(nickname);
	}

	@Override
	public String userIDByEmail(String email) {
		return dao.userIDByEmail(email);
	}

	@Override
	public String printMyProfilePhoto(String user_id) {
		return dao.printMyProfilePhoto(user_id);
	}
	
	@Override
	public Member emailConfirmForPassword(HashMap<String, String> parameters) {
		return dao.emailConfirmForPassword(parameters);
	}
	@Override
	public int resetPassword(Member member) {
		log.debug("임플에서 패스워드 확인{}",member.getPassword());
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		log.debug("임플에서 패스워드 확인 - 암호화 후 {}",member.getPassword());
		return dao.resetPassword(member);
	}
	
	@Override
	public boolean isPasswordMatching(Member member) {;
		String storedEncodedPassword = dao.getEncodedPasswordByUserNum(member.getUser_num());
		return passwordEncoder.matches(member.getPassword(), storedEncodedPassword);
	}
	//----------------[회원가입&로그인 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[멤버태그 기능 시작]----------------------
	//사용자가 설정한 멤버태그 배열을 member_taglink에 insert하는 테이블
	@Override
	public int memberTagLinkInsertArray(String[] tagNameList, int user_num) {
		ArrayList<Integer> tag_num = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();		//insert명령을 수행하기위한 hashmap변수
		ArrayList<Integer> tagList = new ArrayList<>();		//사용자의 모든 지정 태그 배열을 저장하는 변수
		int methodResult = 0;			//제대로 메서드가 수행되었는지 확인을 위한 체커
		//태그명으로 태그번호를 배열로 리턴받는 메서드 실행
		if(tagNameList.length != 0) {
			tag_num.addAll(dao.memberTagSearchByTagNameReturnTagNum(tagNameList));
		}
		log.debug("태그숫자 : {}",tag_num);
		
		map.put("user_num", user_num);					//사용자의 회원번호를 미리 집어넣는다
		
		for(int i =0; i < tag_num.size(); i++) {
			//taglink_member테이블에 넘길 값을 hashmap으로 형성
			//hashmap에서 같은 이름을 갖는 변수를 여러번 입력할경우, 이전 값이 삭제되고 가장 최신값으로 update되기에
			//이 코드에서 remove명령어는 필요없는 것으로 확인했습니다.
			map.put("tag_num", tag_num.get(i));
			log.debug("태그맵 : {}", map);
			//해당 유저가 동일한 값을 설정했는지 확인하는 메서드 실행
			methodResult = dao.memberTagLinkSearch(map);
			if(methodResult==1) {
				//만약 설정된 값이 존재한다면 이미 설정한 상태이기에 해당 태그에 대한 insert 명령어 취소하고 다음 태그로 넘어간다
				log.debug("넘어감");
				continue;
			}
			//해당 태그가 설정된 적이 없다면 insert 메서드 실행
			methodResult = dao.memberTagLinkInsert(map);
			log.debug("삽입함");
		}				//for문 종료
		
		//insert전부 수행 후, 이번에 insert하지 않은 과거에 insert한 tag삭제작업 실행
		//사용자의 user_num을 이용하여 현재 taglink가 걸린 tag_num을 배열로 전부 리턴받는다.
		tagList = dao.memberTagLinkSearchAllByUserNum(user_num);
		
		for(int i=0; i < tag_num.size(); i++) {		//삭제기능 수행을 위한 for문 시작
			int no = tag_num.get(i);			//checker와 중복=이번에 선택한 tag값이기에 해당 값을 변수에 저장
			//변수에 해당하는 tag_num을 tagList에서 제거
			tagList.remove(Integer.valueOf(no));
			log.debug("삭제");
		}				//for문 종료
		if(!tagList.isEmpty()) {
			//taglink_member테이블에서 삭제할 값을 hashmap으로 형성
			map.put("tag_num", tagList);
			//해당 태그객체를 taglink_member 테이블에서 삭제하는 메서드 실행
			methodResult = dao.memberTagLinkDelete(map);
			log.debug("결과 : {}", methodResult);
		}

		//전 과정 종료
		return methodResult;
	}			//memberTagLinkInsertArray메서드 종료
	//----------------[멤버태그 기능 종료]----------->>>>>>>>>>>>
	
	//<<<<<<<<<<<------[마이페이지 기능 시작]----------------------
	//일반 검색
	@Override
	public Member memberSearchById(String user_id) {
		//이 검색으로 회원태그는 출력되지 않습니다.
		return dao.memberSearchById(user_id);
	}
	//마이페이지 검색
	@Override
	public Member memberSearchByMyPage(String user_id) {
		//이 검색으로 회원태그 목록이 같이 출력됩니다.
		Member member = dao.memberSearchById(user_id);
		//태그정보를 추가로 입력
		member.setTagList(dao.memberTagSelectByUserNum(member.getUser_num()));
		log.debug("태그숫자 : {}", member.getTagList());
		
		return member;
	}
	//수정한 프로필 정보를 담은 member 객체를 매개변수로 보내, DB를 수정하는 메서드
	@Override
	public int memberUpdateProfile(Member m) {
		return dao.memberUpdateProfile(m);
	}

	//수정한 계정 정보를 담은 member 객체를 매개변수로 보내, DB를 수정하는 메서드
	@Override
	public int memberUpdateAccount(Member m) {
		return dao.memberUpdateAccount(m);
	}
	
	//특정 유저의 닉네임을 기반으로 검색한 값에서 회원번호만 리턴하는 메서드
	@Override
	public ArrayList<Integer> memberByNicknameReturnUserNum(String nickname){
		return dao.memberByNicknameReturnUserNum(nickname);
	}

	//개인정보 수정 메서드
	@Override
	public int memberUpdatePersonalInfo(HashMap<String, Object> map) {
		log.debug("정보 {} ", map);
		return dao.memberUpdatePersonalInfo(map);
	}
	//----------------[마이페이지 기능 종료]----------->>>>>>>>>>>>
	
	
	//<<<<<<<<<<<------[팔로우 기능 시작]----------------------
	//특정 사용자의 특정 범위의 팔로워 목록을 리턴하는 메서드
	@Override
	public ArrayList<MemberFollow> followerArraySearch(HashMap<String, Object> map){
		return dao.followerArraySearch(map);
	}
	//특정 사용자의 특정 범위의 팔로우 목록을 리턴하는 메서드
	@Override
	public ArrayList<MemberFollow> followeeArraySearch(HashMap<String, Object> map){
		return dao.followeeArraySearch(map);
	}
	//사용자가 특정 회원을 팔로우한 상태인지 확인하는 메서드
	@Override
	public int followCheck(HashMap<String, Object> map) {
		return dao.followSearch(map);
	}
	//사용자가 특정 회원을 팔로우 하는 메서드
	@Override
	public int followInsert(int user_num, int follower_num) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("followee_num", user_num);
		map.put("follower_num", follower_num);
		log.debug("map {}", map);
		int methodResult = dao.followInsert(map);		//팔로우 메서드 실행

		//상대가 자신을 팔로우 했는지 확인하기위한 맵 변수 구성
		HashMap<String, Object> fmap = new HashMap<>();
		fmap.put("followee_num", follower_num);
		fmap.put("follower_num", user_num);
		log.debug("fmap {}", fmap);
		
		int check = dao.followSearch(fmap);		//상대도 자신을 팔로우했는지 확인하는 메서드 실행
		log.debug("체크 {}", check);
		
		if(check !=0) {	//상대도 자신을 팔로우했을 경우, 자신과 상대의 관계를 친구상태로 변경하는 메서드 실행
			methodResult = dao.followFriendUpdate(map);
			methodResult = dao.followFriendUpdate(fmap);
		}
		return methodResult;
	}
	//사용자가 특정 회원을 언팔로우 하는 메서드
	@Override
	public int followDelete(int user_num, int follower_num) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("followee_num", user_num);
		map.put("follower_num", follower_num);
		int methodResult;		//메서드의 정상동작 여부를 확인하는 변수
		//해당 회원과 친구상태였는지 확인하는 메서드
		int check = dao.followSearchReturnFriendCheck(map);

		if(check !=0) {	//상대와 친구상태일경우, 친구상태를 해제하는 메서드 실행
			HashMap<String, Object> fmap = new HashMap<>();
			fmap.put("followee_num", follower_num);
			fmap.put("follower_num", user_num);

			methodResult = dao.followFriendRelease(fmap);
		}
		//해당 회원과의 팔로우 상태를 해제한다.
		methodResult = dao.followDelete(map);
		
		return methodResult;
	}

	//회원번호를 매개변수로 해당 회원과 친구관계인 팔로워 목록을 출력
	@Override
	public ArrayList<MemberFollow> friendSelectByUserNum(int user_num){
		return dao.friendSelectByUserNum(user_num);
	}
	
	@Override
	public int getUserIdByNickname(String nickname) {
		log.debug("닉네임 값 in 임플 : {}", nickname);
		return dao.getUserIdByNickname(nickname);
	}
	@Override
	public String nicknameByUserId(String userId) {
		return dao.nicknameByUserId(userId);
	}
	
	// 유저 고유 번호로 닉네임을 가져온다.
	@Override
	public String getNicknameByUserNum(int user_num) 
	{
		return dao.getNicknameByUserNum(user_num);
	}
	@Override
	public Member memberSearchByUsername(String nickname) {
		Member member = dao.memberSearchByUsername(nickname);
		//태그정보를 추가로 입력
		member.setTagList(dao.memberTagSelectByUserNum(member.getUser_num()));
		log.debug("다른 사람의 페이지 태그숫자 : {}", member.getTagList());
		return member;
	}
	@Override
	public Member memberSearchByNum(int user_num) {
		return dao.memberSearchByNum(user_num);
	}
	@Override
	public List<Map<String, Object>> getAllUserNumsAndNicknamesByChatroomNum(int chatroom_num) {
		return dao.getAllUserNumsAndNicknamesByChatroomNum(chatroom_num);
	}


}
