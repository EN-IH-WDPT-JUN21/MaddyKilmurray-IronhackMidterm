package com.ironhack.midterm.controller.dto.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
