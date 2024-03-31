package com.smart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repo.IContactRepo;

import jakarta.servlet.http.HttpSession;

@Service
public class ContactMgmtServiceImpl implements IContactMgmtService {

	@Autowired
	private IContactRepo contactRepo;
	
	
	@Override
	public Contact addContact(Contact contact) {
		
		return contactRepo.save(contact);
		
	}
	
	
	//get all contacts of login user
	/*
	 * @Override public List<Contact> getContactsByUser(User user) {
	 * 
	 * return contactRepo.findByUser(user);
	 * 
	 * }
	 */
	
	
	@Override
	public Page<Contact> getContactsByUser(User user ,int pageNo) {
	
		Pageable pageable = PageRequest.of(pageNo, 6);
		
		return contactRepo.findByUser(user ,pageable);
		
	}
	
	
	@Override
  public void removeSessionMessage() {
		
		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getSession();
		session.removeAttribute("msg");
		
	}


}
