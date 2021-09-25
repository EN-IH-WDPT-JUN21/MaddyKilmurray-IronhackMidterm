package com.ironhack.midterm.controller.dto;

import com.ironhack.midterm.dao.accounts.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private long id;

    private LocalDate transactionDate;

    private BigDecimal transactionAmount;

    private long transferAccountId;

    private long receivingAccountId;

    public TransactionDTO(BigDecimal transactionAmount, int transferAccountId, int receivingAccountId) {
        this.transactionDate = LocalDate.now();
        this.transactionAmount = transactionAmount;
        this.transferAccountId = transferAccountId;
        this.receivingAccountId = receivingAccountId;
    }

}
