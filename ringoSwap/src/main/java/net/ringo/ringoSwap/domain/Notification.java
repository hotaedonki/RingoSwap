package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
	int noti_num;
	int admin_num;
	String title;
	String contents;
	int views;
	String inputdate;
	String modifie_date;
}
