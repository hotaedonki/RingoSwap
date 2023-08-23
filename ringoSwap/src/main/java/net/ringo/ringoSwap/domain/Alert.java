package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//알림 정보를 저장하는 DB테이블에 데이터를 전송/전달받을때 사용하는 VO객체
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
	int alert_num;
	String alert_type;
	int receiver_num;
	int sender_num;
	int type_num;
	int message_num;
	boolean alert_checked;
	String inputdate;
}
