package com.ironhack.midterm.controller.dto.users;

import com.ironhack.midterm.dao.users.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {

    private long id;

    @NotNull
    private String name;

    private String username;
    private String password;

    private Set<Role> roles;

    public AdminDTO(String name, String username, String password, Set<Role> roles) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
