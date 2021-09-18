package com.ironhack.midterm.dao;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private long houseNumber;
    private String firstLineOfAddress;
    private String secondLineOfAddress;
    private String thirdLineOfAddress;

    private String city;
    private String county;
    private String country;
    private String postcode;
}
