package com.smart.service.serviceImpl;

import com.smart.DTO.UserDTO;
import com.smart.DTO.UserRegisterDTO;
import com.smart.DTO.mapper.UserMapper;
import com.smart.Exception.CustomException;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.service.serviceInterface.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    UserServiceImpl(UserRepository userRepository, UserMapper userMapper,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.getUserByUserName(username)
              .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public User updateUser(UserDTO userDTO, String userName) {
        User existingUser = userRepository.getUserByUserName(userName)
                .orElseThrow(() -> new CustomException("User not found","/user/profile"));
        if(existingUser.getId() != userDTO.getId()){
            throw new CustomException("Invalid user","/user/profile");
        }
        userMapper.updateUserFromDto(userDTO, existingUser); // update in Java Object Graph happens here
        return userRepository.save(existingUser); // now save updated entity in BD and return updated entity
    }

    @Override
    public void saveUser(UserRegisterDTO userRegisterDTO) {
        if(userRegisterDTO == null){
            throw new CustomException("Saving User cannot be null","saving user failed","/signup");
        }
        User user =userMapper.toEntity(userRegisterDTO);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean updatePassword(String oldPassword, String newPassword, String username) {
        User currentUser = this.userRepository.getUserByUserName(username)
                .orElseThrow(()-> new RuntimeException("User Not Found"));
        if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
            currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(currentUser);
            return true;
        }else{
            return false;
        }
    }
}
