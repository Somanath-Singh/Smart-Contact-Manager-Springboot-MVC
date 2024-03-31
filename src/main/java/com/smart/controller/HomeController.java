package com.smart.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.entities.User;
import com.smart.repo.IUserRepo;
import com.smart.service.IEmailService;
import com.smart.service.IUserMgmtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private IEmailService emailService;

	@Autowired
	private IUserMgmtService userService;
	
	@Autowired
	private IUserRepo userRepo;

	@GetMapping("/")
	public String getHome(Model model) {

		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";

	}

	@GetMapping("/about")
	public String getAbout(Model model) {

		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";

	}

	@GetMapping("/signup")
	public String getSignup(Model model) {

		model.addAttribute("title", "Signup - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";

	}
	
	@GetMapping("/signin")
	public String getSigin(Model model) {

		model.addAttribute("title", "Signin - Smart Contact Manager");
		return "login";

	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult validation,
			@RequestParam(value = "agrement", defaultValue = "false") boolean agrement, Map<String, Object> map,
			RedirectAttributes attrs ,HttpServletRequest req) {
		
		// for server side form validation
					if (validation.hasErrors()) {

						System.out.println("ERROR " + validation.toString());
						map.put("user", user);
						return "signup";

					}

		try {
			if (!agrement) {
				System.out.println("You have not agreed the term and conditions");
				throw new Exception("  You have not agreed the term and conditions");
			}
			
			//url
			
			//check the url getting from the browser
			
			String url = req.getRequestURL().toString();
			
			url = url.replace(req.getServletPath(), "");
			

			// add user data into database
			User result = this.userService.registerUser(user ,url);

			map.put("user", new User());

			// message shown into the webpage
			attrs.addFlashAttribute("successmessage", "Successfully Registered   !!");

			return "redirect:/signup";

		} catch (Exception se) {

			se.printStackTrace();
			map.put("user", user);
			attrs.addFlashAttribute("errormessage", "Something Went Wrong !!" + se.getMessage());
			return "redirect:/signup";

		}

	}
	
	
	//verify email
	
	@GetMapping("/verify-email")
	public String verifyUserEmail(@Param("code") String code ,@Param("email") String email , Model m)
	{
		
		User user = userRepo.findByVerificationCode(code);
		
		boolean f = emailService.verifyAccount(code);
		
		//String email = user.getEmail();
		
		if(f)
		{
			
			m.addAttribute("email" , email );
			
			m.addAttribute("msg" , "Your account successfully verified .");
			
		}
		else
		{
			m.addAttribute("email" , email);
			
			m.addAttribute("msg" , "may be your verification code is incorrect or already verified .");
			
		}
		
		return "verifyEmail";
		
	}

}
