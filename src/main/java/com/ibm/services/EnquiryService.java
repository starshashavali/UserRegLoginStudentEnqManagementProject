package com.ibm.services;

import java.util.List;

import com.ibm.binding.DashboardResponse;
import com.ibm.binding.EnquiryForm;
import com.ibm.binding.EnquirySearchCriteria;
import com.ibm.entities.StudentEnqEntity;

public interface EnquiryService {
	
	List<String> getCourseNames();
	
	List<String> getEnquiryStatus();
	
	DashboardResponse getDashboardData(Integer userId);
	
	Boolean saveEnquiry(EnquiryForm form,Integer userId);
	
	List<StudentEnqEntity> getEnquiries(Integer userId,EnquirySearchCriteria search);
	
	StudentEnqEntity getStudentEnq(Integer eId);

}
