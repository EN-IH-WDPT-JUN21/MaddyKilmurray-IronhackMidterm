package com.ironhack.midterm.dao;

import java.math.BigDecimal;

public class Constants {
    public static final BigDecimal PENALTY_FEE = new BigDecimal(40.0);

    public static final BigDecimal SAVINGS_DEFAULT_INTEREST_RATE = new BigDecimal("0.0025");
    public static final BigDecimal SAVINGS_MAXIMUM_INTEREST_RATE = new BigDecimal("0.5");
    public static final BigDecimal SAVINGS_DEFAULT_MINIMUM_BALANCE = new BigDecimal("1000");
    public static final BigDecimal SAVINGS_ABSOLUTE_MINIMUM_BALANCE = new BigDecimal("100");

    public static final BigDecimal CCARD_DEFAULT_INTEREST_RATE = new BigDecimal("0.2");
    public static final BigDecimal CCARD_MINIMUM_INTEREST_RATE = new BigDecimal("0.1");
    public static final BigDecimal CCARD_DEFAULT_CREDITLIMIT = new BigDecimal("100");
    public static final BigDecimal CCARD_MAXIMUM_CREDITLIMIT = new BigDecimal("100000");

    public static final BigDecimal CHECKING_MINIMUM_BALANCE = new BigDecimal("250");
    public static final BigDecimal CHECKING_MAINTENANCE_FEE = new BigDecimal("12");
}
