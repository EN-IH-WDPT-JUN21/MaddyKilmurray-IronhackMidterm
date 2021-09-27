package com.ironhack.midterm.repository.accounts;

import com.ironhack.midterm.dao.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAll();

    Optional<Account> findById(long id);

    @Query(
        value = "SELECT balance FROM account WHERE id = :id",
        nativeQuery = true
    )
    BigDecimal findBalanceById(@Param("id")long id);
}
