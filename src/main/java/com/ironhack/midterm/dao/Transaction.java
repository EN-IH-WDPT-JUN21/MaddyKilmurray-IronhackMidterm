package com.ironhack.midterm.dao;

import com.ironhack.midterm.dao.accounts.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate transactionDate;

    private BigDecimal transactionAmount;

    @ManyToOne
    @JoinColumn(name = "transfer_account_id")
    private Account transferAccount;

    @ManyToOne
    @JoinColumn(name = "receiving_account_id")
    private Account receivingAccount;

    public Transaction(LocalDate timestamp, BigDecimal transactionAmount,
                       Account transferAccount, Account receivingAccount) {
        this.transactionDate = timestamp;
        this.transactionAmount = transactionAmount;
        this.transferAccount = transferAccount;
        this.receivingAccount = receivingAccount;
    }

    public Transaction(BigDecimal transactionAmount,
                       Account transferAccount, Account receivingAccount) {
        this.transactionDate = LocalDate.now();
        this.transactionAmount = transactionAmount;
        this.transferAccount = transferAccount;
        this.receivingAccount = receivingAccount;
    }
}
