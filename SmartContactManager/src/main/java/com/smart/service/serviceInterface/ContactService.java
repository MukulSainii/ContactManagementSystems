package com.smart.service.serviceInterface;

import com.smart.DTO.ContactDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ContactService  {
    public ContactDTO getContactForUser(Integer contactId, String username);
    public ContactDTO getContactById(Integer id);
    public String  uploadImage(MultipartFile file);
    public void deleteImage(String image);
    public void validateFile(MultipartFile file);
    public void saveContact(ContactDTO contactDto, String username);
}
