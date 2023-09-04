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
	
	public String toString()
	{
		return String.format("live_trend_nnum : {%d} / live_num : {%d} / viewer_num : {%d} / inputdate : {%s}"
				, live_trend_nnum, live_num, viewer_num, inputdate);
	}
}
