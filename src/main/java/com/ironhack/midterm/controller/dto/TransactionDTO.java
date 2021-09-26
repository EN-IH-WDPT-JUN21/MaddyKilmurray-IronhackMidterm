package com.ironhack.midterm.controller.dto;

import com.ironhack.midterm.dao.accounts.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    protected Long id;

    protected LocalDateTime transactionDate;

    protected BigDecimal transactionAmount;

    @NotNull
    protected Long transferAccountId;

    @NotNull
    protected Long receivingAccountId;

    public TransactionDTO(BigDecimal transactionAmount, Long transferAccountId, Long receivingAccountId) {
        this.transactionDate = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        this.transactionAmount = transactionAmount;
        this.transferAccountId = transferAccountId;
        this.receivingAccountId = receivingAccountId;
    }

}
