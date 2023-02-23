package com.practice.SpringBootThymeleafOne.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practice.SpringBootThymeleafOne.dao.ContactRepository;
import com.practice.SpringBootThymeleafOne.dao.UserRepository;
import com.practice.SpringBootThymeleafOne.entity.Contact;
import com.practice.SpringBootThymeleafOne.entity.User;
import com.practice.SpringBootThymeleafOne.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
	{
		String userName = principal.getName();
		System.out.println("USERNAME "+userName);
		
		User user = userRepository.getUserByUserName(userName);
		
		System.out.println("USER "+user);
		
		
		model.addAttribute("user", user);
	}
	
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
//		String userName = principal.getName();
//		System.out.println("USERNAME "+userName);
//		
//		User user = userRepository.getUserByUserName(userName);
//		
//		System.out.println("USER "+user);
//		
//		model.addAttribute("user", user);
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}
	
	//open add contact form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact",new Contact());
		
		return "normal/add_contact_form";
	}
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, Principal principal)
	{
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		contact.setUser(user);
		user.getContacts().add(contact);
		this.userRepository.save(user);
		
		//System.out.println("DATA "+contact);
		//System.out.println("added to data base");
		return "normal/add_contact_form";
	}
	
	//show contacts handler
	@GetMapping("/show-contacts")
	public String showContacts(Model model, Principal principal)
	{
		model.addAttribute("title","Show User Contacts");
		//contacts ki list bhejni hai
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		List<Contact> contacts = this.contactRepository.findContactsByUser(user.getId());
		
		model.addAttribute("contacts", contacts);
		
//		String userName = principal.getName();
//		User user = this.userRepository.getUserByUserName(userName);
//		List<Contact> contacts = user.getContacts();
		return "normal/show_contacts";
	}
	
	//delete contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId, Model model, HttpSession session)
	{
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		
		Contact contact = contactOptional.get();
		
		
		
		//check...do it later
		System.out.println("Contact "+contact.getcId());
		
		contact.setUser(null);
		
		this.contactRepository.delete(contact);
		
		System.out.println("DELETED");
		session.setAttribute("message", new Message("Contact deleted successfully", "success"));
		return "redirect:/user/show-contacts";
	}
	
	//open update form handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(Model model)
	{
		model.addAttribute("title","Update Contact");
		return "update_form";
	}
	
	
	
	
	
	
	
	
	
}
