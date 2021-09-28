package com.ironhack.midterm.repository.accounts;

import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyAccountRepository extends JpaRepository<ThirdPartyAccount, Long> {
}
