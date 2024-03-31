package com.smart.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface IContactMgmtService {
	
	public Contact addContact(Contact contact);
	
	//public List<Contact> getContactsByUser(User user);
	
	public Page<Contact> getContactsByUser(User user ,int pageNo);
	
	public void removeSessionMessage();

}
