package com.smart.DTO.mapper;

import com.smart.DTO.UserDTO;
import com.smart.entities.Contact;
import com.smart.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-01T08:34:30+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setName( user.getName() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setRole( user.getRole() );
        userDTO.setImageURL( user.getImageURL() );
        userDTO.setAbout( user.getAbout() );
        userDTO.setEnabled( user.isEnabled() );

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId( dto.getId() );
        user.setName( dto.getName() );
        user.setEmail( dto.getEmail() );
        user.setRole( dto.getRole() );
        user.setImageURL( dto.getImageURL() );
        user.setAbout( dto.getAbout() );
        user.setEnabled( dto.isEnabled() );
        List<Contact> list = dto.getContacts();
        if ( list != null ) {
            user.setContacts( new ArrayList<Contact>( list ) );
        }

        return user;
    }

    @Override
    public void updateUserFromDto(UserDTO dto, User user) {
        if ( dto == null ) {
            return;
        }

        user.setId( dto.getId() );
        if ( dto.getName() != null ) {
            user.setName( dto.getName() );
        }
        if ( dto.getEmail() != null ) {
            user.setEmail( dto.getEmail() );
        }
        if ( dto.getAbout() != null ) {
            user.setAbout( dto.getAbout() );
        }
        if ( user.getContacts() != null ) {
            List<Contact> list = dto.getContacts();
            if ( list != null ) {
                user.getContacts().clear();
                user.getContacts().addAll( list );
            }
        }
        else {
            List<Contact> list = dto.getContacts();
            if ( list != null ) {
                user.setContacts( new ArrayList<Contact>( list ) );
            }
        }
    }
}
