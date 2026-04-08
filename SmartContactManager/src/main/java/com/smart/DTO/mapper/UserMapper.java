package com.smart.DTO.mapper;

import com.smart.DTO.UserDTO;
import com.smart.DTO.UserRegisterDTO;
import com.smart.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "contacts", ignore = true)
    UserDTO toDto(User user);

    @Mapping(target = "contacts",ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "role", constant = "ROLE_USER")
    User toEntity(UserDTO dto);
    User toEntity(UserRegisterDTO userRegisterDTO);
    //convert userDto to Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)  // do not overwrite the existing value in BD, if any value in Dto is null
    @Mapping(target = "role",ignore = true)    //It will be ignored, and existing value remains unchanged.
    @Mapping(target = "enabled",ignore = true)
    @Mapping(target = "imageURL",ignore = true)
    @Mapping(target = "contacts",ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget User user);// MappingTarget : Update THIS existing object instead of creating a new one
}
