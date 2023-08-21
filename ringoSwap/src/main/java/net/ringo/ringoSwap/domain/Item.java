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
}
