package com.ironhack.midterm.repository.accounts;

import com.ironhack.midterm.dao.accounts.accountsubclasses.StudentCheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCheckingAccountRepository extends JpaRepository<StudentCheckingAccount, Long> {
}
