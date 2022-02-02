package com.intershipjava.intershipproject.services.validator;

import com.intershipjava.intershipproject.exceptions.CreditCardExceptions;
import com.intershipjava.intershipproject.exceptions.CustomerExceptions;

import javax.mail.internet.ParseException;
import java.util.List;

public interface ValidatorService {
    boolean validateCreditCardNumberLength(String str) throws CreditCardExceptions;

    boolean validateLuhnCreditCardAlgorithm(String cardNumber) throws CreditCardExceptions;

    boolean isValidCIV(String civ) throws CreditCardExceptions;

    boolean isValidDate(String expireDate) throws ParseException, CreditCardExceptions, java.text.ParseException;

    boolean isValidEmailAddress(String email) throws CustomerExceptions;

    boolean isValidName(String name);

    boolean validateProductsId(List<String> idProductsList);

    boolean validateShoppingCartProducts(List<String> idProductsList);

    boolean validateStock(List<String> idProductsList);

    boolean validateProductOrderListFromDatabase(String customerName, List<String> productsList);
}
