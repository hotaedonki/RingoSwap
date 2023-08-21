package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
