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
	
	public String toString()
	{
		return String.format("{%d} / {%d} / {%s} / {%s} / {%d} / {%s} / {%d} / {%d}"
				, noti_num, admin_num, title, contents, views, inputdate, modifie_date);
	}
}
