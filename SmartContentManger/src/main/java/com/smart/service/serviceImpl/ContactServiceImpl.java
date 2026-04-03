package com.smart.service.serviceImpl;

import com.smart.DTO.ContactDTO;
import com.smart.DTO.mapper.ContactMapper;
import com.smart.Exception.FileValidationException;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    @Value("${file.upload-dir}")
    private String uploadDir;
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
    public String uploadImage(MultipartFile file) {
        if(file == null){
            throw new FileValidationException("File cannot be null");
        }
        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filename = UUID.randomUUID() + "_" +file.getOriginalFilename();
            Path path  = Paths.get(uploadDir,filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        }catch(IOException e){
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public void deleteImage(String fileName) {
        try{
            if (fileName == null) {
                throw new FileValidationException("File name cannot be null");
            }
            Path path = Paths.get(uploadDir,fileName);
            if (!fileName.equals("default.png")) {
                Files.deleteIfExists(path);
            }
        }catch (IOException e){
            throw new RuntimeException("deletion file failed",e);
        }
    }


}
