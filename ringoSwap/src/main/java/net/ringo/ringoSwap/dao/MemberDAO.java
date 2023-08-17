package net.ringo.ringoSwap.dao;

import org.apache.ibatis.annotations.Mapper;

import net.ringo.ringoSwap.domain.Member;

@Mapper
public interface MemberDAO 
{
	public int insertMember(Member m);
}