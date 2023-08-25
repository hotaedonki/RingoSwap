package net.ringo.ringoSwap.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.ringo.ringoSwap.dao.MemberDAO;
import net.ringo.ringoSwap.domain.Member;

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
}
