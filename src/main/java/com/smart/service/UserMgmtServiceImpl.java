package com.smart.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart.entities.User;
import com.smart.repo.IUserRepo;

@Service
public class UserMgmtServiceImpl implements IUserMgmtService {

	@Autowired
	private IUserRepo repo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private IEmailService emailService;
	
	
	@Override
	public User registerUser(User user ,String url) {
		
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageurl("default.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		
		//user email verification logic during user registration
		
		user.setVerifiedEmail(false);
		
		//store random code at user table during registration
		
		user.setVerificationCode(UUID.randomUUID().toString()); 
		
		
		User newUser =  repo.save(user);
		
		
		if(newUser != null)
		{
			
			try {
				
				emailService.sendEmail(newUser , url);
				
			}
			catch(Exception se)
			{
				se.printStackTrace();
			}
			
		}
		
		return newUser;
		
	}
	
	
}
