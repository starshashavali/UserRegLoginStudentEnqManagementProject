package com.ibm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.entities.StudentEnqEntity;
import com.ibm.entities.UserDtlsEntity;

public interface StudentEnquiriesRepository extends JpaRepository<StudentEnqEntity, Integer> {
   
}
