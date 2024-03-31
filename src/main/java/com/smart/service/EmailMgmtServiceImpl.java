package com.smart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.smart.entities.User;
import com.smart.repo.IUserRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailMgmtServiceImpl implements IEmailService {
	
	@Autowired
	private IUserRepo userRepo;
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public boolean sendOTP(String email ,int otp) throws Exception, MessagingException {
		
		//convert otp to character
		String  OTP = "Your OTP : - " +otp ;
		
		String form = "somanathsingh531@gmail.com";
		String to = email;
		String subject = "Verify forgot password ";
		String content = "Dear [[email]],<br>" + "otp for frogot password of your account :<br>"
				+ "<h3>[[otp]]</h3>" + "Thank you,<br>" + "Rupesh";
		
		try {
			
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			
			helper.setFrom(form, "Rupesh");
			helper.setTo(to);
			helper.setSubject(subject);
			
			content = content.replace("[[email]]",  email);
			
			content = content.replace("[[otp]]",  OTP);
			
			helper.setText(content ,true);
			
			mailSender.send(message);
			
			return true;
			
		}
		catch(Exception se) {
			se.printStackTrace();
			
			return false;
		}
		
		
	}

	@Override
public boolean sendEmail(User user, String url) throws Exception, MessagingException {
		
		String form = "somanathsingh531@gmail.com";
		String to = user.getEmail();
		String subject = "Account Verification";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Somanath";
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			
			helper.setFrom(form, "Somanath");
			helper.setTo(to);
			helper.setSubject(subject);
			
			content = content.replace("[[name]]",  user.getName());
			
			String siteUrl = url+"/verify-email?email="+to+"&code=" + user.getVerificationCode();
			
			content = content.replace("[[URL]]", siteUrl);
			
			System.out.println(siteUrl);
			
			helper.setText(content ,true);
			
			mailSender.send(message);
			
			return true;
			
		}
		catch(Exception se) {
			
			se.printStackTrace();
			
			return false;
			
		}
		
		
	}

	@Override
	public boolean verifyAccount(String verificationCode) {
		
		
		User user = userRepo.findByVerificationCode(verificationCode);
		
		if(user == null)
		{
			
			return false;
			
		}
		else
		{
			
			user.setVerifiedEmail(true);
			
			user.setVerificationCode(null);
			
			userRepo.save(user);
			
			return true;
			
		}

		
	}
	
	
	

}
