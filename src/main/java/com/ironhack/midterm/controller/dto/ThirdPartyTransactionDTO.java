package com.ironhack.midterm.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyTransactionDTO {

    private Long id;

    private LocalDate transactionDate;

    private BigDecimal transactionAmount;

    @NotNull
    private Long transferAccountId;

    @NotNull
    private Long receivingAccountId;

    private String receivingSecretKey;

    public ThirdPartyTransactionDTO(BigDecimal transactionAmount, Long transferAccountId, Long receivingAccountId, String secretKey) {
        this.transactionDate = LocalDate.now();
        this.transactionAmount = transactionAmount;
        this.transferAccountId = transferAccountId;
        this.receivingAccountId = receivingAccountId;
        this.receivingSecretKey = secretKey;
    }
}
