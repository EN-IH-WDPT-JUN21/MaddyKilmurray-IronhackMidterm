package com.ironhack.midterm.enums;

public enum AccountType {
    CHECKING("CHECKING"),
    STUDENTCHECKING("STUDENTCHECKING"),
    CREDITCARD("CREDITCARD"),
    SAVINGS("SAVINGS"),
    THIRDPARTY("THIRDPARTY");

    public final String value;

    private AccountType(String value) {
        this.value=value;
    }

    public static AccountType getAccountType(String sValue) {
        for (AccountType command: values()) {
            if(command.value.equals(sValue))
                return command;
        }
        return null;
    }
}
