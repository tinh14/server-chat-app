package org.example.mappers;

import org.example.dtos.UserDTO;
import org.example.entities.UserEntity;
import org.example.utils.FileUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.io.IOException;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    UserDTO toDTO(UserEntity userEntity);
}
