package net.ringo.ringoSwap.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails
{	
	private static final long serialVersionUID = 1L;
	
	int user_num;
	String username;
	String password;
	String nickname;
	String first_name;
	String last_name;
	String gender;
	String birth_date;
	String email;
	String native_language;
	String target_language;
	String introduction;
	String hobbies;
	String rewrite_time;
	int point;
	int cash;
	String role_name;
	String mbti;
	String ranking_open;
	String email_open;
	String follow_open;
	String gps_open;
	String trans_lang;
	boolean enabled;
	
	@Override
	public String toString()
	{
		return String.format
				("{%d} : user_num \n"
				+ "{%s} : username \n"
				+ "{%s} : password \n"
				+ "{%s} : nickname \n"
				+ "{%s} : first_name \n"
				+ "{%s} : last_name \n"
				+ "{%s} : gender \n"
				+ "{%s} : birth_date \n"
				+ "{%s} : email \n"
				+ "{%s} : native_language \n"
				+ "{%s} : target_language \n"
				+ "{%s} : introduction \n"
				+ "{%s} : hobbies \n"
				+ "{%s} : rewrite_time \n"
				+ "{%d} : point \n"
				+ "{%d} : cash \n"
				+ "{%s} : role_name \n"
				+ "{%s} : mbti \n"
				+ "{%s} : ranking_open \n"
				+ "{%s} : email_open \n"
				+ "{%s} : follow_open \n"
				+ "{%s} : gps_open \n"
				+ "{%s} : trans_lang \n"
				+ "{%b} : enabled \n"
								, user_num
								, username
								, password
								, nickname
								, first_name
								, last_name
								, gender
								, birth_date
								, email
								, native_language
								, target_language
								, introduction
								, hobbies
								, rewrite_time
								, point
								, cash
								, role_name
								, mbti
								, ranking_open
								, email_open
								, follow_open
								, gps_open
								, trans_lang
								, enabled);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
	}
	
}