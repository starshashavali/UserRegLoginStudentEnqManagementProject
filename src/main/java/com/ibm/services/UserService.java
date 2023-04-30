package com.ibm.services;

import com.ibm.binding.LoginForm;
import com.ibm.binding.SignupForm;
import com.ibm.binding.UnlockForm;
import com.ibm.entities.UserDtlsEntity;

public interface UserService {

	String createUser(SignupForm form);
	
	String unlockAccount(UnlockForm form);
	
	String resetPassword(String email);
	
	String loginUser(LoginForm form);

}
