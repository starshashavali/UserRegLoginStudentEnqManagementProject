package com.ibm.services.impl;


import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.binding.LoginForm;
import com.ibm.binding.SignupForm;
import com.ibm.binding.UnlockForm;
import com.ibm.entities.UserDtlsEntity;
import com.ibm.repository.UserRepository;
import com.ibm.services.UserService;
import com.ibm.utils.EmailUtils;
import com.ibm.utils.PwdUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private PwdUtils pwdUtils;

	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private HttpSession session;

	@Override
	public String createUser(SignupForm form) {

		UserDtlsEntity entity = repo.findByEmail(form.getEmail());
		/*
		  entity != null" as a condition in an if statement, it means that 
		 the code inside the if block will be executed only if the "entity" variable is not null.
		 */

		if(entity != null) {
			if(entity.getAccountStatus().equalsIgnoreCase("locked")) {
				return "Your account need to be unlocked";
			}
			else if(entity.getAccountStatus().equalsIgnoreCase("unlocked")) {
				return "Email already exists ,Please login !";
			}
		}

		UserDtlsEntity user = new UserDtlsEntity();
		BeanUtils.copyProperties(form, user);

		String pwd = pwdUtils.generatePwd(6);

		StringBuffer sb = new StringBuffer("");

		sb.append("<h1>Hey, "+form.getName()+"</h1>");

		sb.append("<h3>Unlock your account with this temporary password</h3>");

		sb.append("<br>");

		sb.append("<p>Temporary password : <B>" + pwd +"</B> </p>");

		sb.append("<br>");

		sb.append("<a href=\"http://localhost:9098/unlock?mail="+form.getEmail()+"\">Click here to unlock your account </a>");

		/*
		String mailText ="Hey,"+form.getName()+".Your Temporary Password : "+ pwd +
				". Unlock your account with Temporary Password ."+ 
				"http://localhost:8080/unlock?mail="+form.getEmail();
		 */

		emailUtils.sendEmail(form.getEmail(),"Unlock your account ",String.valueOf(sb));

		user.setAccountStatus("Locked");
		user.setPassword(pwd);

		repo.save(user);

		return "Mail sent to " + form.getEmail();
	}
	/*
	
	// @Override
	public boolean signup(SignUpForm form) {
		UserDtlsEntity user = userDtlsRepo.findByEmail(form.getEmail());

		if (user != null) {
			return false;
		}

		// copy data form binding obj to entity obj
		UserDtlsEntity entity = new UserDtlsEntity();
		BeanUtils.copyProperties(form, entity);

		// generate random pwd and set to object
		String tempPwd = PwdUtils.generateRandomPwd();
		entity.setPwd(tempPwd);

		// set account status as locked
		entity.setAccStatus("Locked");

		// insert record
		userDtlsRepo.save(entity);

		// send email to unlock the account
		String to = form.getEmail();
		String subject = "Unlock your Account : Ashok IT";
		StringBuffer body = new StringBuffer("");
		body.append("<h1> Use below temporary pwd to unlock your account</h1>");

		body.append("Temparary pwd : " + tempPwd);

		body.append("<br/>");

		body.append("<a href=\"http://localhost:9098/unlock?email=" + to + "\">Click Here To Unlock Your Account</a>");

		emailUtils.sendEmail(to, subject, body.toString());

		return true;
	}
	
	 */
	

	@Override
	public String unlockAccount(UnlockForm form) {

		UserDtlsEntity user = repo.findByEmail(form.getEmail());

		if(!form.getTempPassword().equals(user.getPassword())) {
			return "Temporary Password is not matching";
		}

		if(!form.getNewPassword().equals(form.getConfirmPassword())) {
			return "Password not matching";
		}

		user.setPassword(form.getNewPassword());
		user.setAccountStatus("Unlocked");

		repo.save(user);

		return "Account unlocked";
	}
/*
	// @Override
	public boolean unlockAccount(UnlockForm form) {

		UserDtlsEntity entity = userDtlsRepo.findByEmail(form.getEmail());

		if (entity.getPwd().equals(form.getTempPwd())) {
			entity.setPwd(form.getNewPwd());
			entity.setAccStatus("UNLOCKED");
			userDtlsRepo.save(entity);
			return true;

		} else {
			return false;
		}

	}

 */
	@Override
	public String resetPassword(String email) {
		UserDtlsEntity user = repo.findByEmail(email);
		if(user == null) {
			return "Account doesn't exist with this email";
		}

		//String pwd = pwdUtils.generatePwd(6);

		StringBuffer sb = new StringBuffer("");

		sb.append("<h1>Hey, "+user.getName()+"</h1>");

		sb.append("<h4>Your Password</h4>");

		sb.append("<B>" + user.getPassword() +"</B>");

		sb.append("<br>");
		
		sb.append("login to your account with this password.");

		//sb.append("<a href=\"http://localhost:8080/unlock?mail="+form.getEmail()+"\">Click here to unlock your account </a>");

		emailUtils.sendEmail(email, "Reset password",String.valueOf(sb));

		//user.setPassword(pwd);
		repo.save(user);

		return "Check your email";
	}

	@Override
	public String loginUser(LoginForm form) {
		UserDtlsEntity entity = repo.findByEmailAndPassword(form.getEmail(),form.getPassword());
		
		if(entity == null) {
			return "Invalid credentials";
		}
		
		if(entity.getAccountStatus().equalsIgnoreCase("locked")) {
			return "Your account need to be unlocked";
		}
		
		session.setAttribute("userID", entity.getUserId());
		
		return "success";
	}

}
/*
	

	public String login(LoginForm form) {

		UserDtlsEntity entity = userDtlsRepo.findByEmailAndPwd(form.getEmail(), form.getPwd());

		if (entity == null) {
			return "Invalid Credentials";
		}
		if (entity.getAccStatus().equals("LOCKED")) {
			return "Your Account Locked";
		}
      
		//create session and store user data in session
		session.setAttribute("userId",entity.getUserId());
		
		
		return "success";
	}

	public boolean forgotPwd(String email) {

		// check record present in db with givem mail
		UserDtlsEntity entity = userDtlsRepo.findByEmail(email);

		// if record not availabe send error msg
		if (entity == null) {
			return false;
		}

		// if record available send pwd to email and send success msg
		String Subject = "Recover Password";
		String body = "Your Pwd :: " + entity.getPwd();

		emailUtils.sendEmail(email, Subject, body);

		return true;
	}
*/