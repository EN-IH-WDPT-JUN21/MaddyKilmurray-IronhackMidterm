package com.ironhack.midterm.repository;

import com.ironhack.midterm.dao.accounts.accountsubclasses.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
}
