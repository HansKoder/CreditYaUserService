package org.pragma.creditya.r2dbc.persistence.user;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "customers", schema = "public")
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    private String username;
    private String password;

}
