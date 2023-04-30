package com.ibm.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.entities.UserDtlsEntity;

public interface UserRepository extends JpaRepository<UserDtlsEntity, Integer> {
	
	UserDtlsEntity findByEmail(String email);
	
	UserDtlsEntity findByEmailAndPassword(String email,String password);

}

