package com.ironhack.midterm.repository;

import com.ironhack.midterm.dao.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAll();

    @Query(
        value = "SELECT balance FROM account WHERE id = :id",
        nativeQuery = true
    )
    BigDecimal findBalanceById(long id);
}
