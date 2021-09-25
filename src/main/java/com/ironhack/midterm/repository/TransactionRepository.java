package com.ironhack.midterm.repository;

import com.ironhack.midterm.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
