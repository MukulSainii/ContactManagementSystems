package com.smart.DTO;

import com.smart.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterDTO {
    private String id;
    @NotBlank(message = "Name is required")
    private String name;
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Password is require")
    private String password;

    private String role;
    private String imageURL;
    private String about;
    @Pattern(
            regexp = "^(\\+91|91)?[6-9][0-9]{9}$",
            message = "Enter a valid Indian phone number"
    )
    private String phoneNumber;
    private String DOB;
    private String address;
    private Gender gender;
    private boolean enabled;
}
