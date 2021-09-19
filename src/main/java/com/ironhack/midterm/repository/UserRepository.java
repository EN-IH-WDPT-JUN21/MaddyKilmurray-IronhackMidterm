package com.ironhack.midterm.repository;

import com.ironhack.midterm.dao.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);

    @Query(
            value = "SELECT account.id, account.balance FROM account " +
                    "INNER JOIN user ON user.id = primary_owner_id " +
                    "OR user.id = secondary_owner_id" +
                    "WHERE user.username = :name",
            nativeQuery = true
    )
    Optional<User> getBalanceByUsername(@Param("name") String name);
}
