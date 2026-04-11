package com.smart.DTO.mapper;

import com.smart.DTO.ContactDTO;
import com.smart.entities.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {
//       @Mapping(source = "category", target = "category")
       ContactDTO toDto(Contact contact);

       List<ContactDTO> toDtoList(List<Contact> contact);
       Contact  toEntity(ContactDTO contactDTO);

       @Mapping(target = "cid", ignore = true)
       void updateEntity(ContactDTO dto, @MappingTarget Contact entity);

}
