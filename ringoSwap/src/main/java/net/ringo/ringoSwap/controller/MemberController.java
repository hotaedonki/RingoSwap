package net.ringo.ringoSwap.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Member;
import net.ringo.ringoSwap.service.EmailService;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.util.PathHandler;
import net.ringo.ringoSwap.util.SessionNameHandler;
import net.ringo.ringoSwap.enums.email.*;
import net.ringo.ringoSwap.enums.member.JoinState;

@Slf4j
@Controller
@RequestMapping(PathHandler.MEMBER)
public class MemberController 
{
	@Autowired
	MemberService service;
	
	@Autowired
	EmailService emailService;
	
	@GetMapping(PathHandler.JOIN)
	public String join(HttpSession session)
	{
		removeAllSessions(session);
		return "memberView/join";
	}
	
	@PostMapping(PathHandler.JOIN)
	public String join(Member m)
	{	
		log.debug("[ Join... ]");
		log.debug(m.toString());
		
		service.insertMember(m);
		
		return "main";
	}
	
	/*
	 *  /taeho/main.html으로 연결되었던 controller를 해당 html을 memberView로 이동시키면서 수정한 컨트롤러 메서드.
	 *  해당 컨트롤러의 연결 Mapping을 수정하면서 이름도 home으로 바꿨습니다.
	 */
	@GetMapping(PathHandler.HOME)
	public String home()
	{
		return "memberView/home";
	}
	
	/*
	 * loginForm.html로 이동하는 메서드입니다. 해당 메서드에서 로그인과 회원가입을 할 수 있습니다.
	 * 
	 * loginForm을 수정했습니다.
	 * controller를 수정하면서 WebSecurityConfig 또한 수정된 사항에 맞게 수정하였습니다.
	 */
	@GetMapping(PathHandler.LOGIN)
	public String login()
	{
		return "memberView/loginForm";
	}

	/*
	 * loginForm.html에 존재하는 회원가입 부문에서 id의 중복체크 이벤트를 실행할때 idCheck.html을 실행시키는 메서드
	 * 해당 html을 실행시킬때 loginForm.html에서 user_id의 value를 xml로 memberDB에 넘겨 중복되는 id가 존재하는지 확인한 후,
	 * 중복 여부에 따라 다음 두가지 리턴으로 나뉩니다.
	 * 중복이 존재하지 않음 : Model을 통해 해당 id값과 사용가능함을 표시하는 문자열을 idCheck.html에 보내고
	 * 		해당 html문서를 새창으로 엽니다.
	 * 중복 존재 : model을 통해 사용가능하지 않음을 표시하는 문자열을 idCheck.html에 보내고 해당 html문서를
	 * 		새장으로 엽니다.
	 */
	@GetMapping(PathHandler.IDCHECK)
	public String idCheck(String user_id, Model model, HttpSession session)
	{
		//service부의 id체크 메서드 실행
		int n = service.idCheck(user_id);
		
		if(n==0) {
			model.addAttribute("result", user_id+"는 사용 가능한 ID입니다.");
			model.addAttribute("searchid", user_id);
			model.addAttribute("accept", true);
			resetSession(session, SessionNameHandler.isVerifiedID, false);
		}else {
			model.addAttribute("result", user_id+"는 이미 존재하는 ID입니다.");
			model.addAttribute("accept", false);
			resetSession(session, SessionNameHandler.isVerifiedID, true);
		}
		return "memberView/idCheck";
	}
	
	@ResponseBody
	@PostMapping(PathHandler.IDCHECK)
	public boolean idCheck(String user_id, HttpSession session)
	{
		//service부의 id체크 메서드 실행
		int result = service.idCheck(user_id);
		
		// 존재하는 아이디가 있을때
		if (result < 0)
		{
			resetSession(session, SessionNameHandler.isVerifiedID, false);
			return false;
		}
		
		resetSession(session, SessionNameHandler.isVerifiedID, true);
		return true;
	}
	
	// 이메일 전송 
	@ResponseBody
	@PostMapping(PathHandler.EMAILCONFIRM)
	public void emailConfirm(String email, HttpSession session) throws Exception
	{
		String verifyCode = emailService.sendVerifyMessage(email);
		resetSession(session, SessionNameHandler.verifyCode, verifyCode, 60);
		log.debug("verifyCode - {}", verifyCode);
	}
	
	@ResponseBody
	@PostMapping(PathHandler.EMAILCONFIRMFORPASSWORD)
	public boolean emailConfirmForPassword(String user_id, String email,
			HttpSession session) throws Exception
	{
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("user_id", user_id);
		parameters.put("email", email);
		
		Member member = service.emailConfirmForPassword(parameters);
		
		if (member == null) 
		{
			return false;
		} 
		else 
		{ 
			String verifyCode = emailService.sendVerifyMessage(email);
			resetSession(session, SessionNameHandler.verifyCode, verifyCode, 180);
			log.debug("verifyCode - {}", verifyCode);
		}
		
		return true;
	}
	
	// 전송 받은 인증 코드가 일치한지 확인
	@ResponseBody
	@PostMapping(PathHandler.CHECKVERIFYCODE)
	public EmailVerifyState checkVerifyCode(String code, HttpSession session)
	{
		log.debug("init checkverifyCode()");
		
		if (session.getAttribute(SessionNameHandler.verifyCode) == null)
		{
			resetSession(session, SessionNameHandler.isVerifiedEmail, false);
			return EmailVerifyState.CHECKINPUT;
		}
		
		String vCode = (String)session.getAttribute(SessionNameHandler.verifyCode);
		log.debug("vCode - {}", vCode);
		log.debug("input code - {}", code);
		
		if (!vCode.equals(code))
		{
			resetSession(session, SessionNameHandler.isVerifiedEmail, false);
	        return EmailVerifyState.INCORRECT;
		}
		
		resetSession(session, SessionNameHandler.isVerifiedEmail, true);
	    return EmailVerifyState.VERIFIED;
	}
	
	@ResponseBody
	@PostMapping(PathHandler.RESETPASSWORD)
	public boolean resetPassword(String password, 
			String user_id) {
		
		Member member = service.memberSearchById(user_id);
		String currentPassword = member.getPassword();
		
		log.debug("패스워드 넣기 전{}", member.getPassword());
		
		member.setPassword(password);
		
		log.debug("새 패스워드 넣은 후{}", member.getPassword());
		service.resetPassword(member);
		
		String newPassword = member.getPassword(); 
		
		if(currentPassword.equals(newPassword)) {
			return false;
		} else {
			return true;
		}
		
	}
	
	@PostMapping(PathHandler.CHECKSESSION)
	public JoinState checkSession(HttpSession session)
	{
		if (session.getAttribute(SessionNameHandler.isVerifiedID) == null)
		{
			log.debug("check the VerifiedID session");
			return JoinState.CHECKID;
		}
		
		if (session.getAttribute(SessionNameHandler.isVerifiedEmail) == null)
		{
			log.debug("check the VerifiedEmail session");
			return JoinState.CHECKEMAIL;
		}
		
		boolean isVerifiedID = (boolean)session.getAttribute(SessionNameHandler.isVerifiedID);
		boolean isVerifiedEmail = (boolean)session.getAttribute(SessionNameHandler.isVerifiedEmail);
		
		if (!isVerifiedID)
			return JoinState.CHECKID;
		
		if (!isVerifiedEmail)
			return JoinState.CHECKEMAIL;
		
		return JoinState.SUCCESS;
	}
	
	// 회원 가입 관련 세션 관리를 위한 함수들
	@ResponseBody
	@PostMapping(PathHandler.REMOVEALLSESSIONJOIN)
	public void removeAllSessionJoin(HttpSession session)
	{
		removeAllSessions(session);
		log.debug("clear sessions!");
	}
	
	private void resetSession(HttpSession session, String name, boolean value)
	{
		session.removeAttribute(name);
		session.setAttribute(name, value);
	}

	private void resetSession(HttpSession session, String name, String value, int lifeTime)
	{
		session.removeAttribute(name);
		session.setAttribute(name, value);
		session.setMaxInactiveInterval(lifeTime);
	}
	
	private void removeAllSessions(HttpSession session)
	{
		session.removeAttribute(SessionNameHandler.isVerifiedID);
		session.removeAttribute(SessionNameHandler.isVerifiedEmail);
		session.removeAttribute(SessionNameHandler.verifyCode);
	}
	//----------------[회원가입&로그인 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[멤버태그 기능 시작]----------------------
	/*
	 * 사용자가 설정한 멤버태그 설정 member_taglink에 insert하는 테이블
	 * 컨트롤러에 연결된 service 메서드가 search/insert/delete 기능의 dao메서드 모두와 연결되어
	 * 해당 기능을 수행하기에 membertag의 link설정에 관한 전 과정을 관여합니다.
	 * 
	 * @shl reply : 아직 해당 기능에 대한 프론트엔드 구현 방법을 논의하지 않았기 때문에, controller 메서드는
	 * 		기본적이고 필수적인 기능만 구현했습니다. 
	 * 		이후 논의를 통해 프론트엔드 구현을 확정하고 그에 따라 controller메서드의 수정이 필요합니다(by2023.08.29)
	 */
	@ResponseBody
	@PostMapping(PathHandler.MEMBERTAGLINKINSERT)
	public void memberTagLinkInsert(String[] tagNameList
					, @AuthenticationPrincipal UserDetails user) {
		//현재 사용자 id를 기반으로 사용자의 user_num을 검색해 리턴
		int user_num = service.memberSearchByIdReturnUserNum(user.getUsername());
		
		//검색한 user_num을 기반으로 설정한 태그 배열을 매개변수로 DB에 전달한다.
		int insertResult = service.memberTagLinkInsertArray(tagNameList, user_num);
	}
	//----------------[멤버태그 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[마이페이지 기능 시작]----------------------
	//마이페이지로 이동
	@GetMapping(PathHandler.MYPAGE)
	public String myPage() 
	{
		return "memberView/myPage";
	}
	
	@ResponseBody
	@PostMapping(PathHandler.MYPAGE)
	public Member myMemberPrint(@AuthenticationPrincipal UserDetails user) {
		Member member = service.memberSearchById(user.getUsername());
		return member;
	}
	
	//프로필 수정 페이지로 이동
	@GetMapping(PathHandler.MODIFYPROFILE)
	public String modifyProfile()
	{
		
		return "memberView/modifyProfile";
	}
	
	@ResponseBody
	@PostMapping(PathHandler.MODIFYPROFILE)
	public void modifyProfile(Member m)
	{
		int methodResult = service.memberUpdateProfile(m);
		log.debug("{} - modifyProfile", m);
	}
	@ResponseBody
	@PostMapping(PathHandler.MODIFYACCOUNT)
	public void modifyAccount(Member m)
	{
		int methodResult = service.memberUpdateAccount(m);
		log.debug("{} - modifyProfile", m);
	}
	//----------------[마이페이지 기능 종료]----------->>>>>>>>>>>>

	//<<<<<<<<<<<------[ 시작]----------------------
}