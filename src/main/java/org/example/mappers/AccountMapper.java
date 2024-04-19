package org.example.mappers;

import org.example.dtos.AccountDTO;
import org.example.entities.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
    AccountDTO toDTO(AccountEntity accountEntity);
}
