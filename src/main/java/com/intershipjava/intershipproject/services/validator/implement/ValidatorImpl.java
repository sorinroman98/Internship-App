package com.intershipjava.intershipproject.services.validator.implement;


import com.intershipjava.intershipproject.constants.Constant;
import com.intershipjava.intershipproject.exceptions.CreditCardExceptions;
import com.intershipjava.intershipproject.exceptions.CustomerExceptions;
import com.intershipjava.intershipproject.exceptions.OrderExceptions;
import com.intershipjava.intershipproject.exceptions.ProductExceptions;
import com.intershipjava.intershipproject.model.Order;
import com.intershipjava.intershipproject.model.Product;
import com.intershipjava.intershipproject.repository.OrderRepository;
import com.intershipjava.intershipproject.repository.ProductRepository;
import com.intershipjava.intershipproject.services.validator.ValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ValidatorImpl implements ValidatorService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public boolean validateCreditCardNumberLength(String str) {
        if (str == null) {
            throw new CreditCardExceptions(Constant.errorMessages.INVALID_CREDIT_CARD);
        }
        if (str.matches("[0-9]+") && str.length() == 16)
            return true;
        throw new CreditCardExceptions(Constant.errorMessages.INVALID_CARD_NUMBER);
    }

    @Override
    public boolean validateLuhnCreditCardAlgorithm(String str) {
        try {
            int[] ints = new int[str.length()];
            for (int i = 0; i < str.length(); i++) {
                ints[i] = Integer.parseInt(str.substring(i, i + 1));
            }
            for (int i = ints.length - 2; i >= 0; i = i - 2) {
                int j = ints[i];
                j = j * 2;
                if (j > 9) {
                    j = j % 10 + 1;
                }
                ints[i] = j;
            }
            int sum = 0;
            for (int anInt : ints) {
                sum += anInt;
            }
            if (sum % 10 == 0) {
                return true;
            }
            throw new CreditCardExceptions(Constant.errorMessages.INVALID_CREDIT_CARD);
        } catch (Exception e) {
            throw new CreditCardExceptions(Constant.errorMessages.INVALID_CREDIT_CARD);
        }
    }

    @Override
    public boolean isValidEmailAddress(String email) {
        if (email == null) {
            throw new CustomerExceptions(Constant.errorMessages.INVALID_EMAIL);
        }

        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            throw new CustomerExceptions(Constant.errorMessages.INVALID_EMAIL);
        }
        return true;
    }

    @Override
    public boolean isValidName(String name) {
        if (name == null) {
            throw new CustomerExceptions(Constant.errorMessages.INVALID_NAME);
        }

        if (name.matches("^[a-zA-Z\\s]*$")) {
            return true;
        }
        throw new CustomerExceptions(Constant.errorMessages.INVALID_NAME);
    }

    @Override
    public boolean isValidCIV(String civ) {

        String regex = "^[0-9]{3,4}$";

        Pattern p = Pattern.compile(regex);

        if (civ == null) {
            throw new CreditCardExceptions(Constant.errorMessages.INVALID_INPUT);
        }

        Matcher m = p.matcher(civ);
        if (m.matches()) {
            return true;
        }
        throw new CreditCardExceptions("Invalid civ number!");
    }

    @Override
    public boolean isValidDate(String expireDate) throws java.text.ParseException {
        if (expireDate == null) {
            throw new CreditCardExceptions(Constant.errorMessages.INVALID_INPUT);
        }
        if (expireDate.matches("^\\d{2}/\\d{2}$")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
            simpleDateFormat.setLenient(false);
            if (simpleDateFormat.parse(expireDate).before(new Date())) {
                throw new CreditCardExceptions("Your card is expired!");
            } else {
                return true;
            }
        }
        throw new CreditCardExceptions(Constant.errorMessages.INVALID_INPUT);
    }

    @Override
    public boolean validateProductsId(List<String> idProductsList) {
        if (idProductsList == null) {
            throw new ProductExceptions(Constant.errorMessages.LIST_NULL);
        }
        for (String s : idProductsList) {
            if (productRepository.findByUuid(s) == null) {
                throw new ProductExceptions(Constant.errorMessages.NOT_FOUND_PRODUCTS);
            }
        }
        return true;
    }

    @Override
    public boolean validateShoppingCartProducts(List<String> idProductsList) {
        if (idProductsList == null) {
            throw new ProductExceptions(Constant.errorMessages.LIST_NULL);
        }

//        for (String productId : idProductsList){
//            for (String productId2 : idProductsList){
//                if (productRepository.findByUuid(productId).getCategory()
//                        .equals(productRepository.findByUuid(productId2).getCategory())){
//                    throw new ProductExceptions("You can add just one product per category in your basket!");
//                }
//            }
//        }



        for (int i = 0; i < idProductsList.size(); i++) {
            for (int j = i + 1; j < idProductsList.size(); j++) {
                if (productRepository.findByUuid(idProductsList.get(i)).getCategory()
                        .equals(productRepository.findByUuid(idProductsList.get(j)).getCategory())) {
                    throw new ProductExceptions("You can add just one product per category in your basket!");
                }
            }
        }

//        boolean result = idProductsList.stream().filter(
//              twoList -> idProductsList.stream()
//                      .anyMatch(one -> productRepository.findByUuid(one).getCategory()
//                              .equals(productRepository.findByUuid(twoList).getCategory()))
//        ).anyMatch(th)
//        map.values()
//                .stream()
//                .filter(a -> stringToBeMatched == a.getField())
//                .findAny()
//                .ifPresent(a -> throw new IllegalArgumentException());
        return true;
    }

    @Override
    public boolean validateStock(List<String> idProductsList) {
        if (idProductsList == null) {
            throw new ProductExceptions(Constant.errorMessages.LIST_NULL);
        }

        for (String s : idProductsList) {
            if (productRepository.findByUuid(s).getQuantity() < 1) {
                throw new ProductExceptions(Constant.errorMessages.PRODUCT_OUT_OF_STOCK);
            }
        }
        return true;
    }

    @Override
    public boolean validateProductOrderListFromDatabase(String customerName, List<String> productsList) {
        if (customerName == null || productsList == null || productsList.isEmpty()) {
            throw new OrderExceptions(Constant.errorMessages.INVALID_NAME);
        }
        List<Order> ordersDatabaseList = orderRepository.findAll();
        for (Order order : ordersDatabaseList) {
            if (customerName.equals(order.getCustomerName())) {
                IntStream.range(0, productsList.size()).filter(j -> IntStream.range(0, order.getProductList().size()).anyMatch(k -> productRepository.findByUuid(productsList.get(j)).getCategory()
                        .equals(productRepository.findByUuid(order.getProductList().get(k).getUuid()).getCategory()))).forEach(j -> {
                    throw new ProductExceptions("You can buy just one product per category!");
                });
            }
        }
        return true;
    }
}
