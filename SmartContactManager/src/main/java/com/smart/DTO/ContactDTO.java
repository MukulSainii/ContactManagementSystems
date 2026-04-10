package com.smart.DTO;

import com.smart.entities.User;
import com.smart.enums.ContactCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@Data
public class ContactDTO {
    private int Cid;

    @NotBlank(message = "Name is required")
    @Pattern(
            regexp = "^[A-Za-z ]{3,}$",
            message = "enter valid name having at least 3 characters"
    )
    private String name;

    @Pattern(
            regexp = "^[A-Za-z]*$",
            message = "Only letters allowed")
    private String secondname;
    private String work;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^(\\+91|91)?[6-9][0-9]{9}$",
            message = "Enter a valid Indian phone number"
    )
    private String phone;
    @NotNull(message = "Please select contact type")
    private ContactCategory category;
    private String image;
    private String description;
    @ToString.Exclude
    private User user;
}
