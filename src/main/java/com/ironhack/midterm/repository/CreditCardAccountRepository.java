package com.ironhack.midterm.repository;

import com.ironhack.midterm.dao.accounts.CheckingAccount;
import com.ironhack.midterm.dao.accounts.CreditCardAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardAccountRepository extends JpaRepository<CreditCardAccount, Long> {
}
