package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repo.IContactRepo;
import com.smart.repo.IUserRepo;
import com.smart.service.IContactMgmtService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private IUserRepo userRepo;

	@Autowired
	private IContactRepo contactRepo;

	@Autowired
	private IContactMgmtService contactService;

	// method for adding common data to response
	@ModelAttribute
	public User getUserData(Model m, Principal p) {

		String username = p.getName();

		User user = userRepo.findByEmail(username);

		m.addAttribute("user", user);

		return user;

	}

	// user dash-board is shown when you enter in the browser
	// "localhost:4041/user/index"
	@GetMapping("/index")
	public String dashboard(Model model) {

		model.addAttribute("title", "User : DashBoard");

		return "user/userDash";

	}

	// open add contact form
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {

		model.addAttribute("title", "Add : Contact");
		model.addAttribute("contact", new Contact());

		return "user/add_contact_form";

	}

	// procession add contact form

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute("contact") Contact contact,
			@RequestParam("imageName") MultipartFile file, Principal principal, HttpSession session) {

		try {
			// get the user object
			String email = principal.getName();
			User user = userRepo.findByEmail(email);

			// processing and uploading file

			if (file.isEmpty()) {
				// if the file is empty then try our message
				System.out.println("File is empty");
				contact.setImage("contact.png");

			} else {
				// upload the file to folder and update the name of the file into the database
				// set the image name to Contact table
				contact.setImage(file.getOriginalFilename());

				// give the name of the folder where image stored
				File saveFile = new ClassPathResource("static/img").getFile();

				// get the path of the image
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				// Files.copy(input, target, option);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("file saved");

			}

			// set the user details to contact table for foreign key purpose
			contact.setUser(user);

			contactService.addContact(contact);// save contacts

			System.out.println("DATA " + contact);

			// success message
			session.setAttribute("msg", "Your contact details is saved successfully .");
			session.setAttribute("type", "alert-info");

		} catch (Exception se) {

			se.printStackTrace();
			// error message
			session.setAttribute("msg", "something went wrong ." + se.getMessage());
			session.setAttribute("type", "alert-danger");

		}

		return "redirect:/user/add-contact";

	}

	/*
	 * @GetMapping("/show-contacts") public String showContacts ,Model m ,Principal
	 * p) {
	 * 
	 * User user = getUserData(m,p);
	 * 
	 * List<Contact> contacts = contactService.getContactsByUser(user);
	 * 
	 * m.addAttribute("contactsList",contacts );
	 * 
	 * return "user/show_contacts";
	 * 
	 * }
	 */

	// show contact handler

	@GetMapping("/show-contacts")
	public String showContacts(@RequestParam(defaultValue = "0") int pageNo, Model m, Principal p) {

		User user = getUserData(m, p);

		Page<Contact> contacts = contactService.getContactsByUser(user, pageNo);

		m.addAttribute("currentPage", pageNo);
		m.addAttribute("totalElements", contacts.getTotalElements());
		m.addAttribute("totalPages", contacts.getTotalPages());

		// m.addAttribute("contactsList",contacts ); //old

		m.addAttribute("contactsList", contacts.getContent());

		return "user/show_contacts";

	}

	// showing perticular contact details

	@GetMapping("/{cid}/contact")
	public String showContactDetails(@PathVariable("cid") Integer cId, Model model, Principal principal) {

		// get the contact object by using contact id
		Optional<Contact> contactOptional = contactRepo.findById(cId);
		Contact contact = contactOptional.get();

		String email = principal.getName();
		User user = userRepo.findByEmail(email);

		if (user.getId() == contact.getUser().getId()) {

			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());

		}

		return "user/contact_details";

	}

	// delete the perticular contact

	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId, Model model, HttpSession session) {

		try {
			// get the contact object

			Contact contact = contactRepo.findById(cId).get();

			// delete the contact image

			File deleteFile = new ClassPathResource("static/img").getFile();

			File file = new File(deleteFile, contact.getImage());

			file.delete();

			// delete the contact
			contactRepo.deleteById(cId);

			// message
			session.setAttribute("msg", "Contact deleted successfully   . . .");
			session.setAttribute("type", "alert-info");

		} catch (Exception se) {
			se.printStackTrace();
		}

		return "user/userDash";

	}

	// open update form handler

	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model model) {
		model.addAttribute("title", "Update : Contact Form ");

		// get the Contact object

		Contact contact = this.contactRepo.findById(cid).get();

		model.addAttribute("contact", contact);

		return "user/update_form";

	}

	// update form processing

	@PostMapping("/process-update")
	public String updateContact(@ModelAttribute("contact") Contact contact,
			@RequestParam("imageName") MultipartFile file, Model model, HttpSession session) {

		try {

			// get the old Contact object
			Contact oldContact = contactRepo.findById(contact.getCid()).get();

			// image
			if (!file.isEmpty()) {
				// file work
				// rewrite

				// delete old phote

				// get the name of the folder where old image stored
				File deleteFile = new ClassPathResource("static/img").getFile();

				File file1 = new File(deleteFile, oldContact.getImage());

				file1.delete();

				// update old photo

				// upload the file to folder and update the name of the file into the database

				// give the name of the folder where image stored
				File saveFile = new ClassPathResource("static/img").getFile();

				// get the path of the image
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				// Files.copy(input, target, option);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				// set the image name to Contact table
				contact.setImage(file.getOriginalFilename());

			} else {

				contact.setImage(oldContact.getImage());

			}

			// set the user object to the new Contact object

			contact.setUser(oldContact.getUser());

			// save the new data of contact

			contactRepo.save(contact);

			// message
			session.setAttribute("msg", "Contact update successfully ...");
			session.setAttribute("type", "alert-success");

		} catch (Exception se) {
			se.printStackTrace();
		}

		System.out.println(contact);

		return "redirect:/user/" + contact.getCid() + "/contact";

	}

	// user profile page

	@GetMapping("/your_profile")
	public String getProfile(Model model) {

		model.addAttribute("title", "Profile : Page");

		return "user/profile";

	}

	// open setting handler
	@GetMapping("/setting")
	public String openSettings() {

		return "user/settings";

	}

	@PostMapping("/change-password")
	public String getChangePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, Principal principal ,HttpSession session) {

		System.out.println(newPassword);
		
		
		String email = principal.getName();

		User loginuser = userRepo.findByEmail(email);

		if (this.bCryptPasswordEncoder.matches(oldPassword, loginuser.getPassword())) {
			// change the password
			loginuser.setPassword(bCryptPasswordEncoder.encode(newPassword));

			userRepo.save(loginuser);

			// message
			session.setAttribute("msg", "Password change successfully ...");
			session.setAttribute("type", "alert-success");
			
			return "redirect:/user/setting";

		} else {

			// error

			// message
			session.setAttribute("msg", "Enter correct password ...");
			session.setAttribute("type", "alert-danger");
			
			return "redirect:/user/setting";

		}

	}

}
