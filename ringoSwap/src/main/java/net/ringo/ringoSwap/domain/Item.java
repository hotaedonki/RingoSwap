package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	int item_num;
	int item_point;
	int item_cash;
	String item_name;
	String item_image_url;
	String inputdate;
	String item_category;
	boolean item_bann;
	
	public String toString()
	{
		return String.format("item_num : {%d} / item_point : {%d} / item_cash : {%d} / item_name : {%s} / item_image_url : {%s} / inputdate : {%s} / item_category : {%s} / item_bann : {%b}"
				,item_num ,item_point ,item_cash ,item_name ,item_image_url ,inputdate ,item_category ,item_bann);
	}
}
