package com.smart.DTO;

import com.smart.entities.Contact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
    private int id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String role;

    private String imageURL;

    private String about;

    private boolean enabled;
    private List<Contact> contacts=new ArrayList<>();
}
