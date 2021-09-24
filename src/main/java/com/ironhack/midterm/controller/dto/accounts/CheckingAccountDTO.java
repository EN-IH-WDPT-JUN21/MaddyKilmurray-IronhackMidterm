//package com.ironhack.midterm.controller.dto.accounts;
//
//import com.ironhack.midterm.dao.Money;
//
//import javax.persistence.AttributeOverride;
//import javax.persistence.AttributeOverrides;
//import javax.persistence.Column;
//import javax.persistence.Embedded;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//
//public class CheckingAccountDTO {
//
//    @NotBlank
//    private String secretKey;
//
//    @NotNull
//    private Money minimumBalance;
//
//    @Embedded
//    private Money monthlyMaintenanceFee;
//}
