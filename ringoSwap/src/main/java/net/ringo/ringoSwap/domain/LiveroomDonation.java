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
}
