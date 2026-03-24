package com.smart.DTO.mapper;

import com.smart.DTO.UserDTO;
import com.smart.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role",ignore = true)
    @Mapping(target = "enabled",ignore = true)
    @Mapping(target = "imageURL",ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget User user);
}
