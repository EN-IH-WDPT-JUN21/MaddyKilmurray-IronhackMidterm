package com.ironhack.midterm.controller.dto.users;

import com.ironhack.midterm.dao.users.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyDTO {

    private long id;

    @NotNull
    private String name;

    private String username;
    private String password;

    private Set<Role> roles;

    @NotBlank
    private String hashedKey;
}
