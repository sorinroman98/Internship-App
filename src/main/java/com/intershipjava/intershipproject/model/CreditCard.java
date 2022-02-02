package com.intershipjava.intershipproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreditCard {

    private String creditCardNumber;

    private String cardHolderName;

    private String expiryDate;

    private String civ;

}
