package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyCash {
	int cash_num;
	int user_num;
	String purchase_category;
	int cash_buy;
	String buytext;
	String inputate;
}
