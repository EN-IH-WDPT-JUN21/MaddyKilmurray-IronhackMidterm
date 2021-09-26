package com.ironhack.midterm.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyTransactionDTO extends TransactionDTO {

    @NotBlank
    private String receivingSecretKey;

    public ThirdPartyTransactionDTO(BigDecimal transactionAmount, Long transferAccountId, Long receivingAccountId, String secretKey) {
        this.transactionDate = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        this.transactionAmount = transactionAmount;
        this.transferAccountId = transferAccountId;
        this.receivingAccountId = receivingAccountId;
        this.receivingSecretKey = secretKey;
    }
}
