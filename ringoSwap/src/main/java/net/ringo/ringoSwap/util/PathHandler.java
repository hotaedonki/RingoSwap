
package net.ringo.ringoSwap.util;

public class PathHandler
{
	// Properties set
	public final static String EMAILPROPERTIES = "classpath:static/properties/email.properties";
	
	// Member
	public final static String MAIN = "main";
	public final static String MEMBER = "member";
	public final static String JOIN = "join";
	public final static String HOME = "home";
	public final static String LOGIN = "login";
	public final static String IDCHECK = "idCheck";
	public final static String EMAILCHECK = "emailCheck";
	public final static String NICKNAMECHECK = "nicknameCheck";
	public final static String USERIDBYEMAIL = "userIDByEmail";
	public final static String EMAILCONFIRM = "emailConfirm";
	public final static String PRINTMYPROFILEPHOTO = "printMyProfilePhoto";
	public final static String CHECKVERIFYCODE = "checkverifycode";
	public final static String GETMEMBER = "getMember";
	public final static String EMAILCONFIRMFORPASSWORD = "emailConfirmForPassword";
	public final static String RESETPASSWORD = "resetPassword";
	public final static String MYPAGE = "myPage";
	public final static String CHECKSESSION = "checkSession";
	public final static String REMOVEALLSESSIONJOIN = "removeAllSessionJoin";
	public final static String MEMBERTAGLINKINSERT= "memberTagLinkInsert";
	public final static String MODIFYPROFILE ="modifyProfile";
	
	// Chat
	public final static String CHAT = "chat";
	public final static String DMCHATWITHROOMID = "dmChat";
	public final static String OPENCHATMAIN = "openChatMain";
	public final static String OPENCHATROOM = "openChatRoom";
	public final static String OPENCHATROOMENTER = "openChatRoomEnter";
	public final static String MODIFYACCOUNT ="modifyAccount";
	public final static String CREATEOPENCHATROOM = "createOpenChatroom";
	public final static String SENDMESSAGE = "sendMessage";
	public final static String DELETEMESSAGE = "deleteMessage";
	public final static String LOADCHATLINKS = "loadChatLinks";
	public final static String LOADCHATROOM = "loadChatRoom";
	public final static String LOADCHATROOMS = "loadChatRooms";
	public final static String LOADMESSAGE = "loadMessage";
	public final static String SEARCHCHATROOMBYLANG = "searchChatroomByLang";
	public final static String GETCREDFORMAKEDMCHATROOM = "getCredForMakeDMChatroom";
	public final static String CREATEDMCHATROOM = "createDMChatroom";
	public final static String CHECKEXISTENCEDMCHATROOM = "checkExistenceDMChatRoom";
	public final static String ENTERDMCHATMAINAFTERCREATEROOM = "enterDMChatMainAfterCreateRoom";
	public final static String GETDMCHATROOMLINK = "getDMChatroomLink";
	public final static String MM_OPENCHATROOMENTER = "/chat/openChatRoomEnter/{chatroomID}";
	public final static String MM_OPENCHATROOMMESSAGE = "/chat/openChatRoom/message/{chatroomID}";
	public final static String MM_DMCHATROOMMESSAGE = "/chat/DMChat/dmMessage/{DMRoomNum}";			
	public final static String MM_LOADJOINEDCHATROOMLISTREALTIME = "/chat/openChatMain/loadJoinedChatroomListRealTime/{chatroom_num}";
	public final static String MM_LOADCHATROOMNUMSBYUSERNUM = "/chat/openChatMain/loadChatRoomNumsByUserNum/{userNum}";
	public final static String MM_SEARCHBYTITLE = "/chat/openChatMain/searchByTitle/{userNum}";
	public final static String MM_LOADDMCHATROOMNUMSBYUSERNUM = "/chat/DMChat/loadDMChatRoomNumsByUserNum/{userNum}";
	public final static String MM_LOADJOINEDDMCHATROOMLISTREALTIME = "/chat/DMChat/loadJoinedDMChatroomListRealTime/{DMRoomNum}";
	public final static String MM_SEARCHBYOTHERNICKNAME = "/chat/DMChat/searchByOtherNickname/{userNum}";
	public final static String ST_OPENCHATROOMMESSAGE = "/sub/chat/openChatRoom/message/{chatroomID}";
	public final static String ST_DMCHATROOMMESSAGE = "/sub/chat/DMChat/dmMessage/{DMRoomNum}";	
	public final static String ST_OPENCHATROOMMESSAGESTATE = "/sub/chat/openChatRoom/message/state/{chatroomID}";
	public final static String ST_LOADJOINEDCHATROOMLISTREALTIME = "/sub/chat/openChatMain/loadJoinedChatroomListRealTime/{chatroom_num}";
	public final static String ST_LOADCHATROOMNUMSBYUSERNUM = "/sub/chat/openChatMain/loadChatRoomNumsByUserNum/{userNum}";
	public final static String ST_SEARCHBYTITLE = "/sub/chat/openChatMain/searchByTitle/{userNum}";
	public final static String ST_LOADDMCHATROOMNUMSBYUSERNUM = "/sub/chat/DMChat/loadDMChatRoomNumsByUserNum/{userNum}";
	public final static String ST_lOADJOINEDDMCHATROOMLISTREALTIME = "/sub/chat/DMChat/loadJoinedDMChatroomListRealTime/{DMRoomNum}";
	public final static String ST_SEARCHBYOTHERNICKNAME = "/sub/chat/DMChat/searchByOtherNickname/{userNum}";
	
	// Setting for Web socket, stomp, sockjs . . .
	public final static String WS_STOMP = "/ws-stomp";
	public final static String RINGO_WS_STOMP= "/ringo/ws-stomp";
}
