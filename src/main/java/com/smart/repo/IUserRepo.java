package com.smart.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entities.User;

public interface IUserRepo extends JpaRepository<User, Integer> {
	
	
	public User findByEmail(String email);
	
	public User findByVerificationCode(String verificationCode);
	
	/*
	 * @Query("select u from User u where u.email = :email") public User
	 * getUserByUserName(@Param("email") String email);
	 */

}
