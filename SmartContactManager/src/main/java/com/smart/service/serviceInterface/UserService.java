package com.smart.service.serviceInterface;

import com.smart.DTO.UserDTO;
import com.smart.DTO.UserRegisterDTO;
import com.smart.entities.User;


public interface UserService {

    public UserDTO getUserByUsername(String username);
    public User updateUser(UserDTO userDTO, String userName);
    public void saveUser(UserRegisterDTO userRegisterDTO);
    public boolean updatePassword(String oldPassword, String newPassword, String username);

}
