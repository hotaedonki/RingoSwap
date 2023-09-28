package net.ringo.ringoSwap.domain;

import java.util.ArrayList;
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
	//not null부
	int user_num;				//계정의 회원번호
	String user_id;				//계정의 id값
	String password;			//계정의 비밀번호
	String nickname;			//계정의 닉네임
	String first_name;			//계정 사용자의 이름
	String last_name;			//계정 사용자의 성
	String gender;				//계정 사용자의 성별
	String birth_date;			//계정 사용자의 생년월일
	String email;					//계정 사용자의 인증 이메일
	String native_lang;			//계정 사용자의 모국어
	String target_lang;			//계정 사용자의 학습언어
	//null 가능 부
	String original_profile;		//프로필 사진 실제 파일 이름
	String saved_profile;		//프로필 사진 저장경로에서의 이름
	String original_background;		//배경사진 실제 이름
	String saved_background;			//배경사진 저장 이름
	String introduction;			//계정의 자기소개문구
	String mbti;					//계정의 MBTI값
	int point;						//계정의 무료재화 수치
	int cash;						//계정의 유료재화 수치
	String rewrite_time;		//수정일자
	String role_name;			//계정의 회원권한값
	String ranking_open;		//계정의 랭킹 공개여부
	String email_open;			//계정의 이메일 공개여부
	String follow_open;		//계정의 팔로우/팔로워 공개여부
	String gps_open;			//계정의 위치주소 공개여부
	String trans_lang;			//계정의 번역언어 값
	boolean enabled;			//계정의 인증 허용여부
	//출력시 추가로 필요한 변수목록
	int fr_count;			//사용자를 팔로워 건 사용자의 수, 팔로워 수
	int fe_count;			//사용자가 팔로워를 건 사용자의 수, 팔로우 수
	String tag_list;
	ArrayList<String>tagList;
	
	@Override
	public String toString()
	{
		return String.format
				("{%d} : user_num {%s} : user_id {%s} : password {%s} : nickname {%s} : first_name \n"
				+ "{%s} : last_name {%s} : gender {%s} : birth_date {%s} : email {%s} : native_lang {%s} : target_lang \n"
				+ "{%s} : original_profile {%s} : saved_profile {%s} : introduction {%s} : mbti \n"
				+ "{%d} : point {%d} : cash {%s} : rewrite_time {%s} : role_name \n"
				+ "{%s} : ranking_open {%s} : email_open {%s} : follow_open {%s} : gps_open {%s} : trans_lang \n"
				+ "{%b} : enabled \n"
				+ "{%d} : fr_count {%d} : fe_count {%s} : tag_list, {%s} : tagList \n, original_background : {%s}, saved_background : {%s}"
						, user_num, user_id, password, nickname, first_name, last_name, gender, birth_date, email, native_lang, target_lang
						, original_profile, saved_profile, introduction, mbti, point, cash, rewrite_time, role_name
						, ranking_open, email_open, follow_open, gps_open, trans_lang, enabled, fr_count, fe_count, tag_list, tagList
						, original_background, saved_background);
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
		return this.user_id;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
	}
}