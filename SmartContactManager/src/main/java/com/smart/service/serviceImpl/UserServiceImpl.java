package com.smart.service.serviceImpl;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

import org.springframework.stereotype.Service;
import com.smart.DTO.UserDTO;
import com.smart.DTO.mapper.UserMapper;
import com.smart.service.serviceInterface.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.getUserByUserName(username)
              .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    } 
}
