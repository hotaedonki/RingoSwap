package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.dao.MemberDAO;
import net.ringo.ringoSwap.domain.Member;

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
		return dao.insertMember(m);
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
	public Member emailConfirmForPassword(HashMap<String, String> parameters) {
		return dao.emailConfirmForPassword(parameters);
	}
	@Override
	public Member memberSearchById(String user_id) {
		return dao.memberSearchById(user_id);
	}
	@Override
	public int resetPassword(Member member) {
		log.debug("임플에서 패스워드 확인{}",member.getPassword());
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		log.debug("임플에서 패스워드 확인 - 암호화 후 {}",member.getPassword());
		return dao.resetPassword(member);
	}
	//----------------[회원가입&로그인 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[멤버태그 기능 시작]----------------------
	//사용자가 설정한 멤버태그 배열을 member_taglink에 insert하는 테이블
	@Override
	public int memberTagLinkInsertArray(ArrayList<Integer> tag_num, int user_num) {
		int tagChecker[] = new int[tag_num.size()];				//사용자가 이번에 지정한 tag가 몇개인지, 어떤 것인지 저장하는 변수(이번에 지정안한 tag 삭제 메서드를 위한 변수)
		HashMap<String, Object> map = new HashMap<>();		//insert명령을 수행하기위한 hashmap변수
		int methodResult = 0;			//제대로 메서드가 수행되었는지 확인을 위한 체커
		ArrayList<Integer> tagList = new ArrayList<>();		//사용자의 모든 지정 태그 배열을 저장하는 변수
		for(int i =0; i < tag_num.size(); i++) {
			//taglink_member테이블에 넘길 값을 hashmap으로 형성
			map.put("user_num", user_num);
			map.put("tag_num", tag_num.get(i));
			//해당 유저가 동일한 값을 설정했는지 확인하는 메서드 실행
			methodResult = dao.memberTagLinkSearch(map);
			if(methodResult==1) {				//만약 설정된 값이 존재한다면 이미 설정한 상태이기에 해당 태그에 대한 insert 명령어 취소하고 다음 태그로 넘어간다
				tagChecker[i] = tag_num.get(i);
				continue;
			}
			//해당 태그가 설정된 적이 없다면 insert 메서드 실행
			methodResult = dao.memberTagLinkInsert(map);
			if(methodResult ==1) {				//제대로 insert가 수행되었다면 tagChecker에 해당 태그번호 저장
				tagChecker[i] = tag_num.get(i);
			}
		}				//for문 종료
		
		//insert전부 수행 후, 이번에 insert하지 않은 과거에 insert한 tag삭제작업 실행
		//사용자의 user_num을 이용하여 현재 taglink가 걸린 tag_num을 배열로 전부 리턴받는다.
		tagList = dao.memberTagLinkSearchAllByUserNum(user_num);
		
		for(int i=0; i < tagChecker.length; i++) {		//삭제기능 수행을 위한 for문 시작
			int no = tagChecker[i];			//checker와 중복=이번에 선택한 tag값이기에 해당 값을 변수에 저장
			
			//변수에 해당하는 tag_num을 tagList에서 제거
			tagList.remove(Integer.valueOf(no));
		}				//for문 종료
		
		for(int i=0; i < tagList.size(); i++) {
			//taglink_member테이블에서 삭제할 값을 hashmap으로 형성
			map.put("user_num", user_num);
			map.put("tag_num", tagList.get(i));

			//해당 태그객체를 taglink_member 테이블에서 삭제하는 메서드 실행
			methodResult = dao.memberTagLinkDelete(map);
		}
		//전 과정 종료
		return methodResult;
	}			//memberTagLinkInsertArray메서드 종료
	
}
