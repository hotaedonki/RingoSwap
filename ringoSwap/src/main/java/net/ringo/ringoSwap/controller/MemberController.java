package net.ringo.ringoSwap.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.domain.Member;
import net.ringo.ringoSwap.domain.MemberFollow;
import net.ringo.ringoSwap.service.EmailService;
import net.ringo.ringoSwap.service.MemberService;
import net.ringo.ringoSwap.util.FileService;
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

	//실제 저장하는 저장경로
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;
	
	@GetMapping(PathHandler.JOIN)
	public String join(HttpSession session)
	{
		removeAllSessions(session);
		return "/memberView/join";
	}
	
	@ResponseBody
	@PostMapping(PathHandler.JOIN)
	public String join(@RequestBody Member m)
	{	
		log.debug("[ Join... ]");
		log.debug(m.toString());
		
		service.insertMember(m);
		
		return "/memberView/home";
	}
	
	/*
	 *  /taeho/main.html으로 연결되었던 controller를 해당 html을 memberView로 이동시키면서 수정한 컨트롤러 메서드.
	 *  해당 컨트롤러의 연결 Mapping을 수정하면서 이름도 home으로 바꿨습니다.
	 */
	@GetMapping(PathHandler.HOME)
	public String home()
	{
		return "/memberView/home";
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

	@ResponseBody
	@PostMapping(PathHandler.IDCHECK)
	public boolean idCheck(String user_id, HttpSession session)
	{
		//service부의 id체크 메서드 실행
		int result = service.idCheck(user_id);
		log.debug("이 유저 아이디가 있는경우 : {}", result);
		// 존재하는 아이디가 있을때
		if (result <= 0) {
			resetSession(session, SessionNameHandler.isVerifiedID, false);
			return false;
		}
		resetSession(session, SessionNameHandler.isVerifiedID, true);
		return true;
	}
	
	//이메일 중복확인
	@ResponseBody
	@PostMapping(PathHandler.EMAILCHECK)
	public boolean emailCheck(String email) {
		int count = service.emailCheck(email);
		log.debug("이메일 존재여부 : {}", count);
		return count > 0;
	}
	
	@ResponseBody
	@PostMapping(PathHandler.NICKNAMECHECK)
	public boolean nicknameCheck(String nickname) {
		int count = service.nicknameCheck(nickname);
		return count > 0;
	}
	
	@ResponseBody
	@PostMapping(PathHandler.USERIDBYEMAIL)
	public String userIDByEmail(String email) {
		String user_id = service.userIDByEmail(email);
		
		return user_id;
	}
	
	@ResponseBody
	@PostMapping(PathHandler.PRINTMYPROFILEPHOTO)
	public String printMyProfilePhoto(@AuthenticationPrincipal UserDetails user) {
		log.debug("프로필 프린트가 되는지~ 안되는지~");
		if (user == null) {
	        return "로그인 중 아님";
	    }
		return user.getUsername();
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
		log.debug("유저 아이디{}", user_id);
		Member member = service.memberSearchById(user_id);
		String currentPassword = member.getPassword();
		log.debug("멤버{}", member);
		log.debug("패스워드 넣기 전{}", member.getPassword());
		
		member.setPassword(password);
		
		log.debug("새 패스워드 넣은 후{}", member.getPassword());
		service.resetPassword(member);
		
		String newPassword = member.getPassword(); 
		
		if (currentPassword != null && currentPassword.equals(newPassword)) {
			return false;
		} else {
			return true;
		}
		
	}
	
	@ResponseBody
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
	public void memberTagLinkInsert(@RequestBody String[] updatedTags
					, @AuthenticationPrincipal UserDetails user) {
		//현재 사용자 id를 기반으로 사용자의 user_num을 검색해 리턴
		int user_num = service.memberSearchByIdReturnUserNum(user.getUsername());
		String[] tagNameList = updatedTags;
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
	
	@GetMapping("/otherPage")
	public String otherPage(@RequestParam(name = "username", required = false) String username, Model model) 
	{
	    if (username != null) {
	        model.addAttribute("username", username);
	    }
	    return "memberView/otherPage";
	}
	
	@GetMapping("/updatePersonalInfo")
	public String updatePersonalInfo(@RequestParam(name = "username", required = false) String username, Model model) 
	{
	    if (username != null) {
	        model.addAttribute("username", username);
	    }
	    return "memberView/updatePersonalInfo";
	}
	
	@ResponseBody
	@PostMapping("/updatePersonalInfo")
	public String updatePersonalInfo(String nickname, String password, String gender, String target_lang
					, String trans_lang, String reveal_follow, @AuthenticationPrincipal UserDetails user) 
	{
		HashMap<String, Object> map = new HashMap<>();
		map.put("nickname", nickname);
		map.put("password", password);
		map.put("user_id", user.getUsername());
		map.put("gender", gender);
		map.put("target_lang", target_lang);
		map.put("trans_lang", trans_lang);
		map.put("reveal_follow", reveal_follow);
		log.debug("정보 {} ", map);

		int methodResult = service.memberUpdatePersonalInfo(map);
		if(methodResult == 0) {
		    return "/ringo/member/updatePersonalInfo";
		}
	    return "/ringo/member/"+PathHandler.MYPAGE;
	}
	
	@ResponseBody
	@PostMapping("checkPassword")
	public Map<String, Object> checkPassword(@AuthenticationPrincipal UserDetails user, String password) {
	    Map<String, Object> response = new HashMap<>();
	    
	    Member member = new Member();
	    int user_num = service.memberSearchByIdReturnUserNum(user.getUsername());
	    member.setUser_num(user_num);
	    member.setPassword(password);
	    
	    boolean isMatching = service.isPasswordMatching(member);
	    log.debug("패스워드 맞는지 확인 : {}", isMatching);
	    
	    if(isMatching) {
	        member = service.memberSearchByNum(user_num);
	        response.put("success", true);
	        response.put("data", member);
	        response.put("message", "Password is correct.");
	    } else {
	        response.put("success", false);
	        response.put("message", "패스워드가 맞지 않습니다.");
	    }
	    
	    return response;
	}


	
	@ResponseBody
	@PostMapping("myMemberPrint")
	public Member myMemberPrint(@AuthenticationPrincipal UserDetails user) {
		log.debug("아이디 {}", user.getUsername());
		Member member = service.memberSearchByMyPage(user.getUsername());
		log.debug("받아오기 {}", member);
		return member;
	}
	//마이페이지의 프로필을 불러오는 메서드
	@GetMapping("memberProfilePrint")
	public void memberProfilePrint(String user_id
					, HttpServletRequest request
					, HttpServletResponse response
					, @AuthenticationPrincipal UserDetails user) {
		log.debug("request 출력 {}", request.getRemoteAddr());
		//해당글의 첨부파일명 확인
		Member member = service.memberSearchById(user_id); //글에 대한 정보를 읽어옴(savedfile 포함)
		//파일의 경로를 이용해서 FileInputStream 객체 생성
		String fullPath = uploadPath+"/"+member.getSaved_profile(); //전체 경로 생성
		
		//response를 통해 파일 전송
		try { //header부분 정보를 주며, 한글 이름일 수도 있으니 UTF-8형식으로 넘긴다.
			response.setHeader("Content-Disposition", " attachment;filename="+ URLEncoder.encode(member.getOriginal_profile(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(); 
		}
		try {
			FileInputStream in = new FileInputStream(fullPath); //input
			ServletOutputStream out = response.getOutputStream(); //output
			//파일 복사
			FileCopyUtils.copy(in, out);
			//스트림 닫기
			in.close();
			out.close();
		}  catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	//마이페이지의 배경사진을 불러오는 메서드
		@GetMapping("memberBackPrint")
		public void memberBackPrint(String user_id
						, HttpServletRequest request
						, HttpServletResponse response) {
			log.debug("request 출력 {}", request.getRemoteAddr());
			//해당글의 첨부파일명 확인
			Member member = service.memberSearchById(user_id); //글에 대한 정보를 읽어옴(savedfile 포함)
			//파일의 경로를 이용해서 FileInputStream 객체 생성
			String fullPath = uploadPath+"/"+member.getSaved_background(); //전체 경로 생성
			
			//response를 통해 파일 전송
			try { //header부분 정보를 주며, 한글 이름일 수도 있으니 UTF-8형식으로 넘긴다.
				response.setHeader("Content-Disposition", " attachment;filename="+ URLEncoder.encode(member.getOriginal_background(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace(); 
			}
			try {
				FileInputStream in = new FileInputStream(fullPath); //input
				ServletOutputStream out = response.getOutputStream(); //output
				//파일 복사
				FileCopyUtils.copy(in, out);
				//스트림 닫기
				in.close();
				out.close();
			}  catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
	//프로필 수정 페이지로 이동
	@GetMapping(PathHandler.MODIFYPROFILE)
	public String modifyProfile()
	{
		
		return "memberView/modifyProfile";
	}
	
	@ResponseBody
	@PostMapping("memberModifyProfile")
	public void memberModifyProfile(String introduction, String target_lang, MultipartFile profileUpload
					, MultipartFile backUpload, @AuthenticationPrincipal UserDetails user)
	{
		int user_num = service.memberSearchByIdReturnUserNum(user.getUsername());
		Member mem = service.memberSearchById(user.getUsername());
		log.debug("회원 {}", mem);
		/*upload가 비어있거나 null이 아닐경우 해당 파일을 업로드 하는 메서드 실행*/
		if(profileUpload != null && !profileUpload.isEmpty()) {
			String deletefile = uploadPath + "/" + mem.getSaved_profile();
			log.debug("삭제경로 {}", deletefile);
			String savedfile = FileService.saveFile(profileUpload, uploadPath);		//새 프로필 사진파일 저장
			log.debug("저장경로 {}", savedfile);
			if(!savedfile.isEmpty()  || savedfile.equals("20230908.jpg")) {
				boolean d = FileService.deleteFile(deletefile);		//기존 프로필 사진파일 삭제
				log.debug("del여부 {}", d);
			}
			mem.setOriginal_profile(profileUpload.getOriginalFilename());
			mem.setSaved_profile(savedfile);
		}
		if(backUpload != null && !backUpload.isEmpty()) {
			String deletefile = uploadPath + "/" + mem.getSaved_background();
			log.debug("삭제경로 {}", deletefile);
			String savedfile = FileService.saveFile(backUpload, uploadPath);		//새 배경 사진파일 저장
			log.debug("저장경로 {}", savedfile);
			if(!savedfile.isEmpty() || savedfile.equals("20230909.jpg")) {
				boolean d = FileService.deleteFile(deletefile);		//기존 배경 사진파일 삭제
				log.debug("del여부 {}", d);
			}
			mem.setOriginal_background(backUpload.getOriginalFilename());
			mem.setSaved_background(savedfile);
		}
		
		mem.setIntroduction(introduction);
		mem.setTarget_lang(target_lang);
		mem.setUser_num(user_num);
		log.debug("출력하냔~~~{}", mem);
		int methodResult = service.memberUpdateProfile(mem);
		log.debug("출력해~~~{}", mem);
	}
	@ResponseBody
	@PostMapping(PathHandler.MODIFYACCOUNT)
	public void modifyAccount(Member m)
	{
		int methodResult = service.memberUpdateAccount(m);
		log.debug("{} - modifyProfile", m);
	}

	//현재 사용중인 사용자의 계정ID값을 리턴하는 메서드
	@ResponseBody
	@PostMapping("currentUserIdSearch")
	public String currentUserIdSearch(@AuthenticationPrincipal UserDetails user)
	{
		return user.getUsername();
	}
	
	//----------------[마이페이지 기능 종료]----------->>>>>>>>>>>>
	
	//로그인된 상태에서만 작동
	@ResponseBody
	@PostMapping("nicknamePrint") 
	public String nicknamePrint(@AuthenticationPrincipal UserDetails user) {
		if (user == null) {
	        return "로그인 중 아님";
	    }
		return service.nicknameByUserId(user.getUsername());	
	}
	
	@ResponseBody
	@PostMapping("goToOtherProfile")
	public Member goToOtherProfile(String nickname) {
	    // username을 기반으로 사용자 정보를 검색하는 서비스 메서드 호출
	    Member member = service.memberSearchByUsername(nickname);
	    log.debug("Username: {}", nickname);
	    log.debug("Member info: {}", member);
	    return member;
	}
	
	//<<<<<<<<<<<------[ 친구관련 시작]----------------------
	//사용자의 회원번호를 매개변수로 해당 회원과 친구관계인 회원의 follow정보를 전부 가져와 리턴하는 메서드
	@ResponseBody
	@PostMapping("friendPrint")
	public ArrayList<MemberFollow> friendPrint(@AuthenticationPrincipal UserDetails user){
		log.debug("유저 {}", user.getUsername());
		int user_num = service.memberSearchByIdReturnUserNum(user.getUsername());
		log.debug("유저번호 {}", user_num);
		ArrayList<MemberFollow> friendList = service.friendSelectByUserNum(user_num);
		log.debug("친구목록 {}", friendList);
		return friendList;
	}
	
	//<<<<<<<<<<------[ 팔로우/팔로워 기능 시작 ]------------------------
	@ResponseBody
	@PostMapping("otherFollowerSearch")
	public ArrayList<MemberFollow> otherFollowerSearch(String nickname, String userId){
		int user_num = service.memberSearchByIdReturnUserNum(userId);
		HashMap<String, Object> map = new HashMap<>();		//followerArraySearch메서드용 해쉬맵 변수
		log.debug("번호 {}", user_num);
		ArrayList<Integer> followerList = service.memberByNicknameReturnUserNum(nickname); 
		if(followerList.isEmpty()) {		//followerList변수가 비어있음 = 검색된 팔로워가 없는 것이므로 이후 검색 메서드 구동은 스킵한다.
			ArrayList<MemberFollow> none = null;
			return none;
		}
		map.put("followee_num", user_num);
		map.put("follower_num", followerList);
		log.debug("맵 {}",map);
		ArrayList<MemberFollow> followerSearch = service.followerArraySearch(map);
		
		return followerSearch;
	}

	@ResponseBody
	@PostMapping("otherFolloweeSearch")
	public ArrayList<MemberFollow> otherFolloweeSearch(String nickname, String userId){
		int user_num = service.memberSearchByIdReturnUserNum(userId);
		HashMap<String, Object> map = new HashMap<>();		//followerArraySearch메서드용 해쉬맵 변수
		log.debug("번호 {}", user_num);
		ArrayList<Integer> followeeList = service.memberByNicknameReturnUserNum(nickname); 
		if(followeeList.isEmpty()) {		//followerList변수가 비어있음 = 검색된 팔로워가 없는 것이므로 이후 검색 메서드 구동은 스킵한다.
			ArrayList<MemberFollow> none = null;
			return none;
		}
		map.put("follower_num", user_num);
		map.put("followee_num", followeeList);
		log.debug("맵 {}",map);
		ArrayList<MemberFollow> followeeSearch = service.followeeArraySearch(map);
		
		return followeeSearch;
	}
}