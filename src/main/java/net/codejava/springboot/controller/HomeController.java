package net.codejava.springboot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.codejava.springboot.dao.UserRepository;
import net.codejava.springboot.entitites.User;
import net.codejava.springboot.helper.Message;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model) 
	{
		model.addAttribute("title","Home - Personnel Management System");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) 
	{
		model.addAttribute("title","About - Personnel Management System");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) 
	{
		model.addAttribute("title","About - Personnel Management System");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	// Handler for registering user
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser( @ModelAttribute("user") User user, Model model, HttpSession session)
	{	
		try {
						
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			System.out.println("USER"+user);
			
			User result = this.userRepository.save(user);
			
			model.addAttribute("user", new User());
			
			session.setAttribute("message", new Message("Registered Successfully!!!","alert-success"));
			return "signup";
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!!!" +e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	
	}
	
	// Handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","Login Page");	
		return "login";
	}
}

