package com.ironhack.midterm.repository;

import com.ironhack.midterm.dao.accounts.accountsubclasses.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {
}
