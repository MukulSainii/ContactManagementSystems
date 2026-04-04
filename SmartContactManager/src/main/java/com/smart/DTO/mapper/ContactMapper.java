package com.smart.DTO.mapper;

import com.smart.DTO.ContactDTO;
import com.smart.entities.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {
//       @Mapping(source = "category", target = "category")
       ContactDTO toDto(Contact contact);
       Contact  toEntity(ContactDTO contactDTO);

}
