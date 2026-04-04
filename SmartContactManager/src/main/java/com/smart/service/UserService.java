package com.smart.service;

import com.smart.DTO.UserDTO;
import com.smart.DTO.mapper.UserMapper;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Transactional(readOnly = true)
    public UserDTO getCurrentUserDto(User user) {
        return userMapper.toDto(user);
    }
}
