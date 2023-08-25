package net.ringo.ringoSwap.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.ringo.ringoSwap.enums.email.*;

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
	public String join()
	{
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
	
	@GetMapping("home")
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
	@GetMapping("login")
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
	@GetMapping("idCheck")
	public String idCheck(String user_id, Model model)
	{
		//service부의 id체크 메서드 실행
		int n = service.idCheck(user_id);
		if(n==0) {
			model.addAttribute("result", user_id+"는 사용 가능한 ID입니다.");
			model.addAttribute("searchid", user_id);
			model.addAttribute("accept", true);
		}else {
			model.addAttribute("result", user_id+"는 이미 존재하는 ID입니다.");
			model.addAttribute("accept", false);
		}
		return "memberView/idCheck";
	}
	
	@PostMapping(PathHandler.EMAILCONFIRM)
	public void emailConfirm(String email, HttpSession session) throws Exception
	{
		String verifyCode = emailService.sendVerifyMessage(email);
		session.removeAttribute("verifyCode");
		session.setAttribute("verifyCode", verifyCode);
		session.setMaxInactiveInterval(60);
	}
	
	@ResponseBody
	@PostMapping(PathHandler.EMAILCONFIRMFORPASSWORD)
	public boolean emailConfirmForPassword(String user_id, String email)
	{
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("user_id", user_id);
		parameters.put("email", email);
		
		Member member = service.emailConfirmForPassword(parameters);
		
		if (member == null)
			return false;
		
		return true;
	}
	
	@ResponseBody
	@PostMapping(PathHandler.CHECKVERIFYCODE)
	public EmailVerifyState checkVerifyCode(String code, HttpSession session)
	{
		if (session.getAttribute("verifyCode") == null)
			return EmailVerifyState.CHECKINPUT;
		
		String vCode = (String)session.getAttribute("verifyCode");
<<<<<<< HEAD
		log.debug("vcode{}", vCode);
		log.debug("그냥code{}", code);
		if (!vCode.equals(code)) 
=======
		
		if (!vCode.equals(code))
>>>>>>> main
	        return EmailVerifyState.INCORRECT;
	    
	    return EmailVerifyState.VERIFIED;
	}
}