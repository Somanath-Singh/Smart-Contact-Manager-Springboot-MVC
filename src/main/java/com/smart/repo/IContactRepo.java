package com.smart.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface IContactRepo extends JpaRepository<Contact, Integer> {

	
	//public List<Contact> findByUser(User user);  //old
	
	//currentPage - page
	//contact per page - 6
	
	public Page<Contact> findByUser(User user ,Pageable pageable);   //new
	
	//search
	public List<Contact> findByNameContainingAndUser(String keyword , User user);
	
	
}
