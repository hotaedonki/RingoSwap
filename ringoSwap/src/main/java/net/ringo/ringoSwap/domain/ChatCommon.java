package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 채팅 메시지에 대한 정보를 담는 클래스 : 채팅 내용에 대한 DTO

채팅 내용은 크게 들어오는 사람에 대한 환영 메시지에 대한 ENTER과 방에 있는 사람들이 채팅을 칠 때 사용하는 TALK 두 가지로 메시지 타입을 나눕니다. 이때 타입은 ENUM으로 선언합니다.

다음으로 어떤 방에서 채팅이 오가는지 확인하기 위한 방번호,채팅을 보낸 사람,메시지,채팅발송 시간 등을 변수로 선언합니다.

여기서 더 나가면 ENTER,TALK 뿐만 아니라 OUT 으로 메시지 타입을 추가해서 나가는 사람에 대한 메시지를 전달해도 OK.
 */

// 메시지 타입 :  입장 채팅
// 메시지 타입에 따라서 동작하는 구조가 달라진다.
// 입장과 퇴장 ENTER 과 LEAVE 의 경우 입장/퇴장 이벤트 처리가 실행되고,
// TALK 는 말 그대로 해당 채팅방을 sub 하고 있는 모든 클라이언트에게 전달됩니다.

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatCommon 
{
	public enum MessageType
	{
		ENTER, TALK, LEAVE
	}
	
	private MessageType type;
	int chat_num;
	int user_num;
	int chatroom_num;
	String message;
	String inputdate;
	String origin_file;
	String saved_file;
	int photo_size;
	
	public String toString()
	{
		return String.format("chat_num : {%d} / user_num : {%d} / chatroom_num : {%d} / photo_size : {%d} / message : {%s} / inputdate : {%s} / origin_file : {%s} / saved_file : {%s}"
				, chat_num, user_num, chatroom_num, photo_size, message, inputdate, origin_file, saved_file);
	}
}
