package net.codejava.springboot.controller;

import java.security.Principal;
import java.util.List;


import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.codejava.springboot.dao.ContactRepository;
import net.codejava.springboot.dao.UserRepository;
import net.codejava.springboot.entitites.ContactDetails;
import net.codejava.springboot.entitites.User;
import net.codejava.springboot.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		
		String userName = principal.getName();
		System.out.println("USERNAME"+userName);
		
		// Get the user using username i.e. email
		
		User user = userRepository.getUserByUserName(userName);
		
		System.out.println("USER"+user);
		
		model.addAttribute("user", user);
		
	}
	
	// dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) 
	{
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}
	
	// Handler  for add in form
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{	
		model.addAttribute("title", "Add Contact");	
		model.addAttribute("contact", new ContactDetails());
		
		return "normal/add_contact_form";
	}
	
	// Handler for processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute ContactDetails contactDetails, Principal principal) 
	{
		
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		contactDetails.setUser(user);
		
		user.getContacts().add(contactDetails);
		
		this.userRepository.save(user);
		
		System.out.println("DATA"+contactDetails);
		
		System.out.println("Added to data base");
		
		
		return "normal/add_contact_form";
	}
	
	// Handler for showing contacts
	@GetMapping("/show-contacts")
	public String showContacts(Model m, Principal principal) 
	{
		m.addAttribute("title","Show User Contacts");
		
		
		String userName = principal.getName();
		
		User user = this.userRepository.getUserByUserName(userName);
		
		List<ContactDetails> contactsDetails= this.contactRepository.findContactsByUser(user.getId());
		
		m.addAttribute("contacts",contactsDetails);
		
		return "normal/show_contacts";
	}
	
	// Handler for deleting contact
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid")Integer cId, Model model, HttpSession session, Principal principal)
	{
		ContactDetails contact = this.contactRepository.findById(cId).get();
		
		System.out.println("Contact "+ contact.getcId());
		
//		contact.setUser(null);
//		
//		this.contactRepository.delete(contact);
		
		User user = this.userRepository.getUserByUserName(principal.getName());
		user.getContacts().remove(contact);
		
		this.userRepository.save(user);
		
		System.out.println("DELETED");
		session.setAttribute("message", new Message("Contact has been deleted successfully!!!", "success"));
		return "redirect:/user/show-contacts";
	}
	
	// Handler for opening update contact
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model m)
	{	
		m.addAttribute("title", "Update Contact");
		
		ContactDetails contact = this.contactRepository.findById(cid).get();
		
		m.addAttribute("contact",contact);
		
		return "normal/update_form";
	}
	
	// Handler for updating contact 
	@RequestMapping(value="/process-update", method = RequestMethod.POST)
	public String udpateHandler(@ModelAttribute ContactDetails contact, Model m, HttpSession session, Principal principal)
	{

			
//			ContactDetails oldContactDetails = this.contactRepository.findById(contact.getcId()).get();
			
		User user = this.userRepository.getUserByUserName(principal.getName());
			
		contact.setUser(user);		
		this.contactRepository.save(contact);
			
		session.setAttribute("message", new Message("Your contact is updated...", "success"));
			

		System.out.println("CONTACT NAME " + contact.getFirstName());
		System.out.println("CONTACT ID " + contact.getcId());
		return "redirect:/user/show-contacts";
	}
	
}
