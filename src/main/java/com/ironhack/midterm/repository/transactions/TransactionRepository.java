package com.ironhack.midterm.repository.transactions;

import com.ironhack.midterm.dao.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
