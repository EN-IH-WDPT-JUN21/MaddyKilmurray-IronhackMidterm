package com.ironhack.midterm.enums;

public enum AccountType {
    CHECKING("CHECKING"),
    STUDENT_CHECKING("STUDENT_CHECKING"),
    CREDIT_CARD("CREDIT_CARD"),
    SAVINGS("SAVINGS"),
    THIRD_PARTY("THIRD_PARTY");

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
