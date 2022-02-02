package com.intershipjava.intershipproject.constants;

public final class Constant {
    private Constant(){}
    public static class errorMessages {

        public static final String NOT_FOUND_PRODUCTS = "NOT PRODUCTS FOUND!";
        public static final String INVALID_NAME = "The name is invalid!";
        public static final String EMAIL_EXIST = "Email already exist!";
        public static final String INVALID_EMAIL = "Invalid email format!";
        public static final String INVALID_INPUT = "Invalid input!";
        public static final String INVALID_CREDENTIALS = "Invalid username or password!";
        public static final String EMPTY_CUSTOMER_LIST = "Empty customers list!";
        public static final String EMPTY_PRODUCT_LIST = "Empty products list!";
        public static final String INVALID_OBJECT = "Invalid object!";
        public static final String EMPTY_PASSWORD = "Password it's empty";
        public static final String FAILED_TO_ENCRYPT = "Failed to encrypt password!";
        public static final String ORDER_NOT_EXIST = "Order doesn't exist";
        public static final String PRODUCT_OUT_OF_STOCK = "Some products from your order are out of stock!";
        public static final String ORDER_ALREADY_PAYED = "Order is already payed!";
        public static final String INVALID_AMOUNT_OF_STOCK = "Quantity can't be less than 1!";
        public static final String INVALID_ENTERED_PRICE = "Price can't be less than 1";
        public static final String PRODUCT_EXIST = "Product already exist!";
        public static final String INVALID_CREDIT_CARD = "Invalid credit card!";
        public static final String LIST_NULL = "List can't be null!";
        public static final String INVALID_CARD_NUMBER = "The credit card number is invalid";
        public static final String INACTIVE_ACCOUNT = "THIS ACCOUNT IT'S NOT ACTIVATED! ";


    }

    public static class AppConstants{

        public static final String TOKEN_INVALID = "INVALID";
        public static final String TOKEN_EXPIRED = "EXPIRED";
        public static final String TOKEN_VALID = "VALID";
        public final static String SUCCESS = "success";
    }
}
