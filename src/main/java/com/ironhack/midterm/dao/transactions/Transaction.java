package com.ironhack.midterm.dao.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm.dao.accounts.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    protected long id;

    protected LocalDateTime transactionDate;

    protected BigDecimal transactionAmount;

    @ManyToOne
    @JoinColumn(name = "transfer_account_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Account transferAccount;

    @ManyToOne
    @JoinColumn(name = "receiving_account_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected Account receivingAccount;

    protected Boolean successful;

    public Transaction(LocalDateTime timestamp, BigDecimal transactionAmount,
                       Account transferAccount, Account receivingAccount) {
        this.transactionDate = timestamp;
        this.transactionAmount = transactionAmount;
        this.transferAccount = transferAccount;
        this.receivingAccount = receivingAccount;
    }

    public Transaction(BigDecimal transactionAmount,
                       Account transferAccount, Account receivingAccount) {
        this.transactionDate = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        this.transactionAmount = transactionAmount;
        this.transferAccount = transferAccount;
        this.receivingAccount = receivingAccount;
    }
}
