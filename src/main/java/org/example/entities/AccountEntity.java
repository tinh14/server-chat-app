package org.example.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AccountEntity {

    @Id
    @Column(name = "account_username", length = 32)
    private String username;

    @Column(name = "account_password", length = 32)
    private String password;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "account_user_id")
    private UserEntity user;
}
