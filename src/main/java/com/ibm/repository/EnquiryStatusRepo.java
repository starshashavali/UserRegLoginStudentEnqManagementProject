package com.ibm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ibm.entities.EnquiryStatus;

public interface EnquiryStatusRepo extends JpaRepository<EnquiryStatus, Integer> {
	
	@Query("select distinct(statusName) from EnquiryStatus")
	List<String> getStatusNames();

}
