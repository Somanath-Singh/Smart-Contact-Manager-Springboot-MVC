package com.smart.service;

import com.smart.entities.User;

import jakarta.mail.MessagingException;

public interface IEmailService {
	
	public boolean sendOTP(String email ,int otp) throws Exception, MessagingException;

	public boolean sendEmail(User user , String url) throws Exception, MessagingException;
	
	public boolean verifyAccount(String verificationCode);
	
}
