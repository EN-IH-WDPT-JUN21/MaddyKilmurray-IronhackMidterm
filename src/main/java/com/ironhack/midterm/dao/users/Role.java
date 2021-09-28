package com.ironhack.midterm.dao.users;

import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Role(String roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString() {
        return this.roleType;
    }
}
