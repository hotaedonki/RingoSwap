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
}
