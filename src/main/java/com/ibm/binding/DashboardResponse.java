package com.ibm.binding;

import lombok.Data;

@Data
public class DashboardResponse {
	
	private Integer noOfEnquiries;
	private Integer enrolledEnquiries;
	private Integer lostEnquiries;

}
