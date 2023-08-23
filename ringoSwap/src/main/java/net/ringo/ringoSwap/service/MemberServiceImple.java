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
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	//회원가입 메서드
	@Override
	public int insertMember(Member m) 
	{
		m.setPassword(passwordEncoder.encode(m.getPassword()));
		return dao.insertMember(m);
	}
	//id중복체크 메서드
	@Override
	public int idCheck(String user_id) {
		return dao.idCheck(user_id);
	}
}
