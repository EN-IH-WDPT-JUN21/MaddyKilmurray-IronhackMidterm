package com.ironhack.midterm.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ThirdPartyTransaction extends Transaction {

    @ManyToOne
    @JoinColumn(name = "transfer_account_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ThirdPartyAccount transferAccount;

    private String secretKey;

    public ThirdPartyTransaction(LocalDateTime timestamp, BigDecimal transactionAmount,
                       ThirdPartyAccount transferAccount, Account receivingAccount) {
        this.transactionDate = timestamp;
        this.transactionAmount = transactionAmount;
        this.transferAccount = transferAccount;
        this.receivingAccount = receivingAccount;
    }

    public ThirdPartyTransaction(BigDecimal transactionAmount,
                       ThirdPartyAccount transferAccount, Account receivingAccount) {
        this.transactionDate = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        this.transactionAmount = transactionAmount;
        this.transferAccount = transferAccount;
        this.receivingAccount = receivingAccount;
    }
}
