package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Liveroom {
	int live_num;
	int host_num;
	String live_title;
	boolean live_cam;
	String live_lang;
	int viewer_max;
	
	public String toString()
	{
		return String.format("live_num : {%d} / host_num : {%d} / live_title : {%s} / live_cam : {%b} / live_lang : {%s} / viewer_max : {%d}"
				, live_num, host_num, live_title, live_cam, live_lang, viewer_max);
	}
}
