package com.smart.service.serviceInterface;

import com.smart.DTO.ContactDTO;
import org.springframework.data.domain.Page;

public interface ContactService  {
    public ContactDTO getContactForUser(Integer contactId, String username);
    public ContactDTO getContactById(Integer id);
    public void saveContact(ContactDTO contactDto, String username);
    public Page<ContactDTO> getContacts(Integer page, String username);
    public void deleteContact(Integer cid, String username);
    public void updateContact(String fileName, String username, ContactDTO contactDTO);
}
