package com.smart.controller;

import com.smart.DTO.ContactDTO;
import com.smart.DTO.UserDTO;
import com.smart.DTO.mapper.UserMapper;
import com.smart.entities.User;
import com.smart.enums.ContactCategory;
import com.smart.helper.ImageUtil;
import com.smart.helper.Message;
import com.smart.helper.SecurityUtils;
import com.smart.service.serviceInterface.ContactService;
import com.smart.service.serviceInterface.PaymentService;
import com.smart.service.serviceInterface.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	private final UserMapper userMapper;
	private final ContactService contactService;
	private final UserService userService;
    private final PaymentService paymentService;
	
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
	@GetMapping("/index")
    public String dashBoard(Model model) {
		model.addAttribute("title","User home(DashBoard)");
		return "normal/user_dashboard";
    }
	
	// Add_contact Form handler
	@GetMapping("/addContact")
	public String showingAddContactForm(Model model) {
		model.addAttribute("title","Add Contact"); 
		model.addAttribute("contactDTO",new ContactDTO());
		model.addAttribute("contactTypes", ContactCategory.values());
		return "normal/addContact";
	}
	
	
	/**handler for adding new contact,
	 * if Image present upload into system and store name into db
	 * add contact info into database along with user
	 * after adding successfully again show addContact.html page for add another contact */
	@PostMapping("/addContact")
	public String addContact(@Valid @ModelAttribute ContactDTO contactDto, BindingResult result,
								  @RequestParam("profileImage")MultipartFile file,Principal principal,
								  HttpSession session,Model model
	) {
		if(result.hasErrors()){
			model.addAttribute("contactTypes", ContactCategory.values());
			return "normal/addContact";
		}
		//processing the image
		if(!file.isEmpty()) {
			String fileName = ImageUtil.uploadImage(file);
			contactDto.setImage(fileName);
		}
		contactService.saveContact(contactDto, principal.getName());
		session.setAttribute("message",  new Message("your contact is added!! Add more ..","success"));
		return "redirect:/user/addContact";
	}

	/*
	* Handler for showing contact listing with pagination
	* using Offset and limit
	* page variable holding current page value*/
	@GetMapping("/show_contact/{page}")
	public String showContact(@PathVariable("page") Integer page,Model model,Principal principal) {
		Page<ContactDTO> contacts = contactService.getContacts(page,principal.getName());
		model.addAttribute("currentPage",page);
		model.addAttribute("contacts",contacts);
		model.addAttribute("totalPages",contacts.getTotalPages());
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
   @PostMapping("/delete/{Cid}")
	public String deleteContact(@PathVariable("Cid") Integer Cid,Model model,HttpSession session,Principal principal) {
	   contactService.deleteContact(Cid, principal.getName());
	   session.setAttribute("message",new Message("contact delete successfully..!!","success"));
		return"redirect:/user/show_contact/0";
	}

	/*handler for update form*/
	@GetMapping("/update_contact/{id}")
	public String showUpdateContactForm(@PathVariable Integer id, Model model) {
		model.addAttribute("tittle", "update_contact");
		model.addAttribute("contact", contactService.getContactById(id));
		model.addAttribute("contactTypes", ContactCategory.values());
		return "normal/update_form";
	}

	/*handler for update contact
	* image store in multipart file
	* contact data store in Contact variable
	* */
	@PostMapping("/update_contact")
	public String updateContact(@ModelAttribute ContactDTO contactDTO,@RequestParam("profileImage")MultipartFile file,HttpSession session,Model model,Principal principal) {
		String imageName ;
		ContactDTO oldContactDetails = contactService.getContactById(contactDTO.getCid());
			if(!file.isEmpty()) {
				ImageUtil.deleteImage(oldContactDetails.getImage());
				imageName = ImageUtil.uploadImage(file);
			}else {
				imageName = oldContactDetails.getImage();
			}
		    contactService.updateContact(imageName,principal.getName(), contactDTO);
			session.setAttribute("message", new Message("your contact is updated", "success"));
		return "redirect:/user/contact/"+contactDTO.getCid();
	}

	//handler for profile
	@GetMapping("/profile")
	public String yourProfile(Model model) {
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
        boolean status = userService.updatePassword(oldPassword,newPassword,principal.getName());
		if(status){
			session.setAttribute("message", new Message("your password successfully change", "success"));
		}else{
			session.setAttribute("message", new Message("your old password is not match", "danger"));
			return "redirect:/user/setting";
		}
		return "redirect:/user/index";
	}

	//handler for first time create order for payment
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object>data,Principal principal) throws Exception {
		return paymentService.createOrder(data, principal.getName());
	}

	//handler for update order data in data base
	@PostMapping("/update_order")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object>data){
		paymentService.updatePayment(data);
		return ResponseEntity.ok(Map.of("msg","updated"));
	}

	/*
	* this method update login user profile data
	* along with update security context which hold login user data
	* during update check email should be unique
	* */
	@PostMapping("/profile")
	public String showProfileEdit(@Valid @ModelAttribute("user") UserDTO userDto, BindingResult result, HttpSession session, Principal principal){
		if(result.hasErrors()) {
			session.setAttribute("message",new Message("field is required!!","danger"));
			return "/user/profile";
		}
		SecurityUtils.updateCurrentUser(userService.updateUser(userDto,principal.getName()));
		session.setAttribute("message",new Message("profile update successfully..!!","success"));
	   return "redirect:/user/profile";
	}
}

