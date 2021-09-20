package com.ironhack.midterm.dao;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private long houseNumber;
    private String streetName;
    private String city;
    private String postcode;
    private String country;
}
