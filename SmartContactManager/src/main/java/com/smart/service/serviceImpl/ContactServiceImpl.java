package com.smart.service.serviceImpl;

import com.smart.DTO.ContactDTO;
import com.smart.DTO.mapper.ContactMapper;
import com.smart.Exception.CustomException;
import com.smart.Exception.NotFoundException;
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

import java.util.List;

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
                .orElseThrow(() -> new CustomException("Contact not found","showing contact failed, please try again","/user/show_contact/"+contactId));
        User user = userRepository.getUserByUserName(username)
                .orElseThrow(() -> new CustomException("User not found","showing contact failed, please try again","/user/show_contact/"+contactId));
        if (user.getId() != contact.getUser().getId()) {
            throw new CustomException("Unauthorized access","/user/show_contact/"+contactId);
        }
        return contactMapper.toDto(contact);
    }

    @Override
    public ContactDTO getContactById(Integer id){
        Contact contact =  contactRepository.findById(id)
                .orElseThrow(() -> new CustomException("contact is not found","/user/contact"+id));
        return contactMapper.toDto(contact);
    }
    @Override
    public Page<ContactDTO> getContacts(Integer page, String username) {
        int pageSize = 5;
        User user = userRepository.getUserByUserName(username)
                .orElseThrow(()->new CustomException("user not found, methodName = getContacts","showing Contact list failed, please try again","normal/show_contact"));
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Contact> contactsPage = contactRepository.findContactsByUser(user.getId(),pageable);
        return contactsPage.map(contactMapper::toDto);
    }
    @Override
    public void saveContact(ContactDTO contactDto, String username) {
        User user = userRepository.getUserByUserName(username)
                .orElseThrow(() -> new CustomException("User not found","Saving contact failed, please try again","/user/addContact"));
          
        Contact contact = contactMapper.toEntity(contactDto);
        contact.setUser(user);
        user.getContacts().add(contact); // for consistency of Java object graph
        userRepository.save(user);
    }

    @Override
    public void updateContact(String fileName, String username, ContactDTO contactDTO) {
        User user = userRepository.getUserByUserName(username)
                .orElseThrow(()-> new CustomException("User Not Found","Update failed, please try again","user/upate_contact/"+ contactDTO.getUser().getId()));
        Contact contact = contactRepository.findById(contactDTO.getCid())
                .orElseThrow(() -> new CustomException("Contact Not Found", "Update failed", "contact/update/" + contactDTO.getCid()));
        contactMapper.updateEntity(contactDTO, contact);
        if (fileName != null) {
            contact.setImage(fileName);
        }
        contact.setUser(user);
        contactRepository.save(contact);
    }

    @Override
    public void deleteContact(Integer cid, String username) {
       User user = userRepository.getUserByUserName(username)
                .orElseThrow(()-> new CustomException("user not found","Deletion failed, Try Again","/user/show_contact/0"));
       Contact contact =  contactRepository.findById(cid)
               .orElseThrow(()-> new CustomException("contact not found","Deletion failed, Try Again","/user/show_contact/0"));
       if(user.getId() != contact.getUser().getId()){
           throw new CustomException("unauthorized", "/user/show_contact/0");
       }
       user.getContacts().remove(contact);
       userRepository.save(user);
    }

    @Override
    public List<ContactDTO> searchContact(String searchQuery, String username) {
        User user = userRepository.getUserByUserName(username)
                .orElseThrow(()->new NotFoundException("user not found","searching failed, please try again"));
        List<Contact> contact = contactRepository.findByNameContainingAndUser(searchQuery, user);
        return contactMapper.toDtoList(contact);
    }
}
