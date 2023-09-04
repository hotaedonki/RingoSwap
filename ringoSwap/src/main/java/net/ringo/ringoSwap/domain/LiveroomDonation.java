package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveroomDonation {
	int live_donation_num;
	int live_num;
	int host_num;
	int guest_num;
	int donation_cash;
	String inputdate;
	
	public String toString()
	{
		return String.format("live_donation_num : {%d} / live_num : {%d} / host_num : {%d} / guest_num : {%d} / donation_cash : {%d} / inputdate : {%s}"
				,live_donation_num ,live_num ,host_num ,guest_num ,donation_cash ,inputdate);
	}
}
