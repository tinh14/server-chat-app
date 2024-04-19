package org.example.mappers;

import javax.annotation.processing.Generated;
import org.example.dtos.AccountDTO;
import org.example.entities.AccountEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-20T00:04:27+0700",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountDTO toDTO(AccountEntity accountEntity) {
        if ( accountEntity == null ) {
            return null;
        }

        AccountDTO.AccountDTOBuilder accountDTO = AccountDTO.builder();

        accountDTO.username( accountEntity.getUsername() );
        accountDTO.password( accountEntity.getPassword() );

        return accountDTO.build();
    }
}
