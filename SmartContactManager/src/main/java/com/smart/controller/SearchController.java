package com.smart.controller;

import com.smart.DTO.ContactDTO;
import com.smart.service.serviceInterface.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private ContactService contactService;
     
     //search handler
     @GetMapping("/search/{query}")
     public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal){
         List<ContactDTO> contact = contactService.searchContact(query,principal.getName());
    	 return ResponseEntity.ok(contact);
     }
	
	
}
//
//@RestController
//public class SearchController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ContactRepository contactRepository;
//
//    @GetMapping("/search/{query}")
//    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal,
//                                    @RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage) {
//        try {
//            User user = this.userRepository.getUserByUserName(principal.getName());
//            List<Contact> contact = this.contactRepository.findByNameContainingAndUser(query, user);
//            return ResponseEntity.ok(contact);
//        } catch (NumberFormatException e) {
//            // Handle the case where currentPage is not a valid integer
//            return ResponseEntity.badRequest().body("Invalid currentPage parameter");
//        } catch (Exception e) {
//            // Handle other exceptions if needed
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
//        }
//    }
//}
//
