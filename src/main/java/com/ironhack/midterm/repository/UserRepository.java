package com.ironhack.midterm.repository;

import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);

    @Query(
            value = "SELECT SUM(account.balance) FROM account \n" +
                    "INNER JOIN user ON ((user.user_id = primary_owner_id) AND (user.user_id = secondary_owner_id)) \n" +
                    "WHERE user.username = :name AND account.account_type = :accountType",
            nativeQuery = true
    )
    Long getBalanceByUsername(@Param("name") String name, @Param("accountType")AccountType type);
}
