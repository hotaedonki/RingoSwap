package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewerTrend {
	int live_trend_nnum;
	int live_num;
	int viewer_num;
	String inputdate;
}
