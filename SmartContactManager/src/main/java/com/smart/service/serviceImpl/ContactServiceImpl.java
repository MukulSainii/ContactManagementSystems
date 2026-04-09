package com.smart.service.serviceImpl;

import com.smart.DTO.ContactDTO;
import com.smart.DTO.mapper.ContactMapper;
import com.smart.Exception.CustomException;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.service.serviceInterface.ContactService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;

    @Override
    public ContactDTO getContactForUser(Integer contactId, String username) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        User user = userRepository.getUserByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getId() != contact.getUser().getId()) {
            throw new RuntimeException("Unauthorized access");
        }
        return contactMapper.toDto(contact);
    }

    @Override
    public ContactDTO getContactById(Integer id){
        Contact contact =  contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("contact is not found"));
        return contactMapper.toDto(contact);
    }
    @Override
    public Page<ContactDTO> getContacts(Integer page, String username) {
        int pageSize = 5;
        User user = userRepository.getUserByUserName(username)
                .orElseThrow(()->new CustomException("user not found, methodName = getContacts","showing listing failed","normal/show_contact"));
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Contact> contactsPage = contactRepository.findContactsByUser(user.getId(),pageable);
        return contactsPage.map(contactMapper::toDto);
    }
    @Override
    public void saveContact(ContactDTO contactDto, String username) {
        User user = userRepository.getUserByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
          
        Contact contact = contactMapper.toEntity(contactDto);
        contact.setUser(user);
        user.getContacts().add(contact); // for consistency of Java object graph
        userRepository.save(user);
    }

    @Override
    public void updateContact(String fileName, String username, ContactDTO contactDTO) {
        User user = this.userRepository.getUserByUserName(username)
                .orElseThrow(()-> new CustomException("User Not Found","Update failed, please try again",""));
        Contact contact =contactMapper.toEntity(contactDTO);
        contact.setImage(fileName);
        contact.setUser(user);
        user.getContacts().add(contact); // for consistency of Java Object Graph
        contactRepository.save(contact);
    }

    @Override
    public void deleteContact(Integer cid, String username) {
       User user = userRepository.getUserByUserName(username)
                .orElseThrow(()-> new CustomException("user not found","Deletion failed, Try Again "));
       Contact contact =  contactRepository.findById(cid)
               .orElseThrow(()-> new RuntimeException("contact not found"));
       if(user.getId() != contact.getUser().getId()){
           throw new CustomException("unauthorized", "/user/show_contact/0");
       }
       user.getContacts().remove(contact);
       userRepository.save(user);
    }

    @Override
    public ContactDTO searchContact(String searchQuery, String username) {
        userRepository.getUserByUserName(username)
                .orElseThrow(()->new CustomException("user not found","searching failed, please try again",""));
        return null;
    }
}
