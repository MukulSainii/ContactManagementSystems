package com.smart.controller;

import java.io.File;
import com.razorpay.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import com.smart.DTO.ContactDTO;
import com.smart.DTO.UserDTO;
import com.smart.DTO.mapper.ContactMapper;
import com.smart.DTO.mapper.UserMapper;
import com.smart.enums.ContactCategory;
import com.smart.helper.SecurityUtils;
import com.smart.service.ContactService;
import com.smart.service.UserService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.MyOrderRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.MyOrder;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private MyOrderRepository myOrderRepository;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ContactMapper contactMapper;
	@Autowired
	private ContactService contactService;

	@Autowired
	private UserService userService;

	
  /*
   *  This annotation will make this method to run before every handler of this controller class
   *  This method is used for set profile name in the header
   *  instead for fetching user for every request and pass in the model to show login username, use this resuable code
   *  */
	@ModelAttribute
	public void addCommonData(Model model) {
		User user = SecurityUtils.getCurrentUser();
		UserDTO dto = userMapper.toDto(user);
		model.addAttribute("user", dto);
	}
	
	
	//dashBoard handler
	@RequestMapping("/index")
    public String dashBoard(Model model,Principal principal) {
		model.addAttribute("title","User home(DashBoard)");
		return "normal/user_dashboard";
    }
	
	// Add_contact Form handler
	@GetMapping("/addContact")
	public String AddContactForm(Model model) {
		
		//this title show on page tab above on url
		model.addAttribute("title","Add Contact"); 
		
		// this contact object send to addContact.html page form tag (th:object)
		model.addAttribute("contactDTO",new ContactDTO());
		model.addAttribute("contactTypes", ContactCategory.values());
		return "normal/addContact";
	}
	
	
	/**processing contact form,after adding successfully again show addContact.html page for add another contact */
	@PostMapping("/process-contact")
	public String  processContact(@Valid @ModelAttribute ContactDTO contactDto, BindingResult result,                        //form ke ander jo field h uska sara data contact variable me aa jayga
								  @RequestParam("profileImage")MultipartFile file  ,     //for store the file which client upload on server(addContact.html)
								  Principal principal,
								  HttpSession session,RedirectAttributes redirectAttributes,Model model
	) {
		try {
			//user ka data DataBase me save karne ke liye phle user chayye hoga jo login h 
			//use nikalne ke liye security se principal class hamri help karti h
			//ye user ka unique id nikal ke de dega
			if(result.hasErrors()){
//				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
				System.out.println("binding result : "+result);
				model.addAttribute("contactTypes", ContactCategory.values());
				return "normal/addContact";
			}
			String name=principal.getName();
			
			//on this method we write query for fetching data from DB of give unique id,all data store in user
			User user=this.userRepository.getUserByUserName(name)
					.orElseThrow(() -> new RuntimeException("User not found"));
			//processing the image
			  if(file.isEmpty()) {
				  //if file is empty then print message 
				  System.out.println("file i empty");
				  contactDto.setImage("contact.png");
			  }else {
				//otherwise store the file in folder and update its url in database
				  contactDto.setImage(file.getOriginalFilename());
				  
				  //this  class save file in given folder
				  File sfile = new ClassPathResource("static/Image").getFile();
				  
				  Path path = Paths.get(sfile.getAbsolutePath()+File.separator+file.getOriginalFilename()); //here you can add date also for unique name of file every time ,even the same image

//				  Files.copy(pathSource, pathTarget, copyOption)--this method upload image in DataBAse
				  Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
				  System.out.println("image upload successfully");
			}
			
			
			//by-directional mapping h to contact bhi save karna hoga
			contactDto.setUser(user);
			System.out.println("contact DTO_to_entity : "+contactMapper.toEntity(contactDto));
			//getContects method give list of all contact ,and add method add this contact to this user
			user.getContacts().add(contactMapper.toEntity(contactDto));
			
			//save this user in database
			this.userRepository.save(user);
			System.out.println("DATA :" +contactDto);
			//success message 
			session.setAttribute("message",  new Message("your contact is added!! Add more ..","success"));
		} catch (Exception e) {
			System.out.println("ERROR:" + e.getMessage());
			e.printStackTrace();
			//failure message 
			session.setAttribute("message", new Message("Somthing went wrong!! ", "danger"));
		}
		return "redirect:/user/addContact";
	}
	
	     //show contact handler
         //per page5[n]-where n is no. of page
	     //current page=[0]
			@GetMapping("/show_contact/{page}")
			public String showContact(@PathVariable("page") Integer page,Model model,Principal principal) {
				String userUniqueId=principal.getName();   //this get email of user  from database through which user is login
				User user = this.userRepository.getUserByUserName(userUniqueId)
						 .orElseThrow(()-> new RuntimeException("User Not Found"));// repository fire query and get all detail of user of this email
				
				//page  no. ,page size
				Pageable pageable = PageRequest.of(page,5);
				
				Page contacts = (Page) this.contactRepository.findContactsByUser(user.getId(),pageable);// this repository fire query and get all contacts of given id from dataBase
				model.addAttribute("contacts",contacts); // this send data to view page 
				model.addAttribute("currentPage",page); // this send data to view page 
				model.addAttribute("totalPages",contacts.getTotalPages()); // this send data to view page 
				return "normal/show_contact";
			}
	
            /* Handler for show particular contact details */
			@GetMapping("/contact/{Cid}")
			public String showContactDetails(@PathVariable("Cid")Integer Cid,Model model,Principal principal) {
				ContactDTO contact =	contactService.getContactForUser(Cid,principal.getName());
				model.addAttribute("contact",contact);
				return "normal/contact_detail";
			}
			
	//handler for delete contact
			@GetMapping("/delete/{Cid}")
			public String deleteContact(@PathVariable("Cid") Integer Cid,Model model,HttpSession session,Principal principal) {
				//find user through id from url in data database by fire query
				Optional<Contact> contactOptional = this.contactRepository.findById(Cid);
				    System.out.println(Cid);
				if (contactOptional.isPresent()) {

					Contact contact = contactOptional.get();
					
//					//unlink user from contact
//					contact.setUser(null);
//					System.out.println("contact:"+contact);
//					
//					//delete contact
//					this.contactRepository.delete(contact);
					
					//removing bug from delete contact
					  User user = this.userRepository.getUserByUserName(principal.getName())
							  .orElseThrow(()-> new RuntimeException("User Not Found"));
					   user.getContacts().remove(contact);  //is contact ki id se jo contact match kar jayga from contacts use remove kar dega ye method
					   this.userRepository.save(user);
					
					
					    System.out.println("delete successfully");
					//send success message to user through session
					session.setAttribute("message",new Message("contact delete successfully..!!","success"));
				} else {
				    // Handle the case where the contact with the given ID was not found
					System.out.println("given id is not found!!");
				}
				return"redirect:/user/show_contact/0";
			}

			/*handler for update form*/
			@GetMapping("/update_contact/{id}")
			public String showUpdateForm(@PathVariable Integer id, Model model) {
				model.addAttribute("tittle", "update_contact");
				model.addAttribute("contact", contactService.getContactById(id));
				return "normal/update_form";
			}

			/*handler for update contact
			* image store in multipart file
			* contact data store in Contact variable
			* */
			@PostMapping("/process-update")
			public String updateContact(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file,HttpSession session,Model model,Principal principal) {
					ContactDTO oldContactDetails = contactService.getContactById(contact.getCid());
					if(!file.isEmpty()) {
						contactService.deleteImage(oldContactDetails.getImage());
						String imageName = contactService.uploadImage(file);
						contact.setImage(imageName);
					}else {
						contact.setImage(oldContactDetails.getImage());
					}

					User user = this.userRepository.getUserByUserName(principal.getName())
							.orElseThrow(()-> new RuntimeException("User Not Found"));
					contact.setUser(user);
					this.contactRepository.save(contact);
					session.setAttribute("message", new Message("your contact is updated", "success"));
				return "redirect:/user/contact/"+contact.getCid();
			}
			
			
			//handler for profile
			@GetMapping("/profile")
			public String yourProfile(Model model) {
//				if (!model.containsAttribute("profileForm")) {
//					User currentUser = (User) model.asMap().get("user"); // already set by addCommonData
//					model.addAttribute("profileForm", currentUser);
//				}
				return "normal/profile";
			}
			
			//open setting handler
			@GetMapping("/setting")
			public String openSetting() {
				return "normal/setting";
			}
			
			//change password handler
			@PostMapping("/changePassword")
			public String changePassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,Principal principal,HttpSession session) {
				System.out.println("Old Password :-"+oldPassword);
				System.out.println("new Password :-"+newPassword);
				User currentUser = this.userRepository.getUserByUserName(principal.getName())
						.orElseThrow(()-> new RuntimeException("User Not Found"));
				if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
					//change password
					currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
					this.userRepository.save(currentUser);
					session.setAttribute("message", new Message("your password successfully change", "success"));
				}
				else {
					//error
					session.setAttribute("message", new Message("your old password is not match", "danger"));
					return "redirect:/user/setting";
					
				}
				return "redirect:/user/index";
				
				
			}
			
			//handler for first time create order for payment
			@PostMapping("/create_order")
			@ResponseBody
			public String createOrder(@RequestBody Map<String, Object>data,Principal principal) throws Exception {
				System.out.println("order function executed");
				System.out.println(data);
				int amt=Integer.parseInt(data.get("amount").toString());
				System.out.println("amt is : "+amt);
				//create client(key,secret) ==from rozarpay site which we generate
				 var client= new RazorpayClient("rzp_test_aetInMhOh05zJ3", "3XoB97MoSvRzLHjcmf7zTTFN");
				 //creating order
				 JSONObject object=new JSONObject();
				 object.put("amount", amt*100);//our amount in rupees ,but we have convert it into paise
				 object.put("currency", "INR");
				 object.put("receipt", "txn_23456");
				
				 Order order = client.orders.create(object);
				 System.out.println("created order is :- "+order);
				 
				 // save order in your database
				 MyOrder myOrder=new MyOrder();
				 myOrder.setAmount(order.get("amount")+"");
				 myOrder.setOrderId(order.get("id"));
				 myOrder.setPaymentId(null);
				 myOrder.setStatus("created");
				 myOrder.setReceipt(order.get("receipt"));
				 myOrder.setUser(this.userRepository.getUserByUserName(principal.getName())
						               .orElseThrow(()-> new RuntimeException("User Not Found"))
				               );
				 
				 this.myOrderRepository.save(myOrder);
				 
				return order.toString();
			}
			
			//handler for update order data in data base
			@PostMapping("/update_order")
			public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object>data){
				//get order id from data base where data you want to update	
				MyOrder myOrder = this.myOrderRepository.findByOrderId(data.get("order_id").toString());	
				myOrder.setPaymentId(data.get("payment_id").toString());
				myOrder.setStatus(data.get("status").toString());
				this.myOrderRepository.save(myOrder);
				
				System.out.println(data);
				return ResponseEntity.ok(Map.of("msg","updated"));
			}

			/*
			* this method update login user profile data
			* along with update security context which hold login user data
			* during update check email should be unique
			* */
			@PostMapping("/profile")
			public String showProfileEdit(@Valid @ModelAttribute("user") UserDTO userDto, BindingResult result, HttpSession session, Principal principal, RedirectAttributes redirectAttributes){
				if(result.hasErrors()) {
					System.out.println("i am here inside the error");
//					redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
					redirectAttributes.addFlashAttribute("profileForm", userDto);
					session.setAttribute("message",new Message("not updated field is required!!","danger"));
					return "redirect:/user/profile";
				}
				User existingUser = userRepository.getUserByUserName(userDto.getEmail())
						.orElseThrow(() -> new RuntimeException("User not found"));
				// Only check DB if email changed
				String loggedInEmail = principal.getName();
				if (!loggedInEmail.equals(userDto.getEmail())) {
					if (existingUser != null && existingUser.getId() != userDto.getId()) {
						System.out.println("i am  2 ");
						session.setAttribute("message", new Message("Email already exists!", "danger"));
						return "redirect:/user/profile";
					}
				}
				//to avoid overwrite unintended data,manually update only those fields you want to update
				existingUser.setEmail(userDto.getEmail());
				existingUser.setName(userDto.getName());
				existingUser.setAbout(userDto.getAbout());
				User updatedUser = userRepository.save(existingUser);
				SecurityUtils.updateCurrentUser(updatedUser);
				session.setAttribute("message",new Message("profile update successfully..!!","success"));
		       return "redirect:/user/profile";
			}
}

