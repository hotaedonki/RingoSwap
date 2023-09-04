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
	
	public String toString()
	{
		return String.format("cash_num : {%d} / user_num : {%d} / purchase_category : {%s} / cash_buy : {%d} / buytext : {%s} / inputate : {%s}"
				, cash_num, user_num, purchase_category, cash_buy, buytext, inputate);
	}
}
