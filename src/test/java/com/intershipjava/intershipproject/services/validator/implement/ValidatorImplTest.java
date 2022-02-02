package com.intershipjava.intershipproject.services.validator.implement;

import com.intershipjava.intershipproject.exceptions.CreditCardExceptions;
import com.intershipjava.intershipproject.exceptions.CustomerExceptions;
import com.intershipjava.intershipproject.exceptions.OrderExceptions;
import com.intershipjava.intershipproject.exceptions.ProductExceptions;
import com.intershipjava.intershipproject.model.Order;
import com.intershipjava.intershipproject.model.Product;
import com.intershipjava.intershipproject.repository.OrderRepository;
import com.intershipjava.intershipproject.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ValidatorImplTest {


    @Mock
    ProductRepository productRepository;

    @Mock
    OrderRepository orderRepository;


    @InjectMocks
    private ValidatorImpl validator;

    @Test
    void validateCreditCardNumberLength() {
        assertThrows(CreditCardExceptions.class, () -> validator.validateCreditCardNumberLength(null));

        assertThrows(CreditCardExceptions.class, () -> validator.validateCreditCardNumberLength("42325213213"));
    }

    @Test
    void creditCardNumberLengthTest() {
        assertTrue(validator.validateCreditCardNumberLength("1234567890123456"));

    }

    @Test
    void throwInvalidLuhnCreditCardAlgorithmInputTest() {
        assertThrows(CreditCardExceptions.class, () -> validator.validateLuhnCreditCardAlgorithm(null));

        assertThrows(CreditCardExceptions.class, () -> {
            validator.validateLuhnCreditCardAlgorithm("914049603342356354");
        });
    }

    @Test
    void luhnCreditCardAlgorithmTest() {
        assertTrue(validator.validateLuhnCreditCardAlgorithm("4140497047171070"));

    }

    @Test
    void throwInvalidEmailAddressExceptionInputTest() {
        assertThrows(CustomerExceptions.class, () -> {
            validator.isValidEmailAddress(null);
        });

        assertThrows(CustomerExceptions.class, () -> {
            validator.isValidEmailAddress("sorin.com");
        });
    }

    @Test
    void emailAddressTest() {
        assertTrue(validator.isValidEmailAddress("sorin@yahoo.com"));
    }

    @Test
    void throwInvalidNameExceptionInputTest() {

        assertThrows(CustomerExceptions.class, () -> {
            validator.isValidName(null);
        });

        assertThrows(CustomerExceptions.class, () -> {
            validator.isValidName("sorin123");
        });
    }

    @Test
    void nameValidTest() {
        assertTrue(validator.isValidName("Sorin"));
    }

    @Test
    void throwInvalidCivExceptionInputTest() {
        assertThrows(CreditCardExceptions.class, () -> {
            validator.isValidCIV(null);
        });

        assertThrows(CreditCardExceptions.class, () -> {
            validator.isValidCIV("54333");
        });

        assertThrows(CreditCardExceptions.class, () -> {
            validator.isValidCIV("asdxz3");
        });
    }

    @Test
    void civValidateTest() {
        assertTrue(validator.isValidCIV("4213"));
    }

    @Test
    void throwInvalidDateExceptionInputTest() {
        assertThrows(CreditCardExceptions.class, () -> {
            validator.isValidDate(null);
        });

        assertThrows(CreditCardExceptions.class, () -> {
            validator.isValidDate("11/11/2020");
        });

        assertThrows(CreditCardExceptions.class, () -> {
            validator.isValidDate("11/20");
        });

    }

    @Test
    void dateValidTest() throws ParseException {
        assertTrue(validator.isValidDate("12/22"));
    }

    @Test
    void throwInvalidProductsIdExceptionInputTest() {
        List<String> productsList = new ArrayList<>();
        productsList.add("1234");
        Product product = new Product();
        product.setUuid("1234");
        product.setCategory("Laptop");
        when(productRepository.findByUuid("54354ccd3")).thenReturn(null);
        assertThrows(ProductExceptions.class, () -> {
            validator.validateProductsId(productsList);
        });
    }

    @Test
    void throwNullInvalidProductsIdExceptionInputTest() {
        assertThrows(ProductExceptions.class, () -> {
            validator.validateProductsId(null);
        });
    }

    @Test
    void productsIdInputTest() {
        List<String> productsList = new ArrayList<>();
        productsList.add("1234");
        Product product = new Product();
        product.setUuid("1234");
        product.setCategory("Laptop");
        when(productRepository.findByUuid(anyString())).thenReturn(product);

        assertTrue(validator.validateProductsId(productsList));

    }

    @Test
    void ShoppingCartProductsTest_throwSameCategoryException() {

        List<String> productsList = new ArrayList<>();
        productsList.add("1234");
        productsList.add("1235");
        Product product = new Product();
        product.setUuid("1234");
        product.setCategory("Laptop");
        when(productRepository.findByUuid(anyString())).thenReturn(product);

        assertThrows(ProductExceptions.class, () -> {
            validator.validateShoppingCartProducts(productsList);
        });
    }

    @Test
    void ShoppingCartProductsTest_returnTrue() {

        List<String> productsList = new ArrayList<>();
        productsList.add("1234");
        productsList.add("1235");
        Product product = new Product();
        product.setUuid("1234");
        product.setCategory("Laptop");
        Product product2 = new Product();
        product2.setUuid("12345");
        product2.setCategory("Televizor");
        when(productRepository.findByUuid("1234")).thenReturn(product);
        when(productRepository.findByUuid("1235")).thenReturn(product2);

        assertTrue(validator.validateShoppingCartProducts(productsList));

    }

    @Test
    void ShoppingCartProductsTest_returnNullPointerException() {

        assertThrows(ProductExceptions.class, () -> {
            validator.validateShoppingCartProducts(null);
        });
    }


    @Test
    void validateStock_listIsNull() {
        assertThrows(ProductExceptions.class, () -> {
            validator.validateStock(null);
        });
    }

    @Test
    void validateStock_productIsOutOfStock() {
        List<String> productsList = new ArrayList<>();
        productsList.add("1234");
        Product product = new Product();
        product.setUuid("1234");
        product.setQuantity(0);
        when(productRepository.findByUuid(anyString())).thenReturn(product);

        assertThrows(ProductExceptions.class, () -> {
            validator.validateStock(productsList);
        });
    }

    @Test
    void validateStock_true() {
        List<String> productsList = new ArrayList<>();
        productsList.add("1234");
        Product product = new Product();
        product.setUuid("1234");
        product.setQuantity(2);
        when(productRepository.findByUuid(anyString())).thenReturn(product);

        assertTrue(validator.validateStock(productsList));
    }

    @Test
    void validateProductOrderListFromDatabase_isNull() {
        assertThrows(OrderExceptions.class, () -> {
            validator.validateProductOrderListFromDatabase(null, null);
        });
    }

    @Test
    void validateProductOrderListFromDatabase_ProductNotExistInOtherOrderFromSameCustomer() {

        Product product = new Product();
        product.setUuid("1234");
        product.setCategory("Telefon");
        when(productRepository.findByUuid("1234")).thenReturn(product);

        Product product2 = new Product();
        product2.setUuid("123455");
        product2.setCategory("Computer");

        when(productRepository.findByUuid("123455")).thenReturn(product2);

        Product product3 = new Product();
        product3.setUuid("123456");
        product3.setCategory("Masina");
        when(productRepository.findByUuid("123456")).thenReturn(product3);

        Product product4 = new Product();
        product4.setUuid("666");
        product4.setCategory("Computer");

        List<String> productsList = new ArrayList<>();
        productsList.add("6666");
        productsList.add("7555");
        productsList.add("8888");

        when(productRepository.findByUuid("6666")).thenReturn(product4);
        when(productRepository.findByUuid("7555")).thenReturn(product4);
        when(productRepository.findByUuid("8888")).thenReturn(product4);


        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);
        productList.add(product3);

        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        order.setProductList(productList);

        order.setCustomerName("Daniel");
        orderList.add(order);

        when(orderRepository.findAll()).thenReturn(orderList);

        assertThrows(ProductExceptions.class, () -> {
            validator.validateProductOrderListFromDatabase("Daniel", productsList);
        });
    }

    @Test
    void validateProductOrderListFromDatabase_True() {

        Product product = new Product();
        product.setUuid("1234");
        product.setCategory("Telefon");
        when(productRepository.findByUuid("1234")).thenReturn(product);

        Product product2 = new Product();
        product2.setUuid("123455");
        product2.setCategory("Computer");

        when(productRepository.findByUuid("123455")).thenReturn(product2);

        Product product3 = new Product();
        product3.setUuid("123456");
        product3.setCategory("Masina");
        when(productRepository.findByUuid("123456")).thenReturn(product3);

        Product product4 = new Product();
        product4.setUuid("666");
        product4.setCategory("Televizor");

        List<String> productsList = new ArrayList<>();
        productsList.add("6666");
        productsList.add("7555");
        productsList.add("8888");

        when(productRepository.findByUuid("6666")).thenReturn(product4);
        when(productRepository.findByUuid("7555")).thenReturn(product4);
        when(productRepository.findByUuid("8888")).thenReturn(product4);


        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);
        productList.add(product3);

        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        order.setProductList(productList);

        order.setCustomerName("Daniel");
        orderList.add(order);

        when(orderRepository.findAll()).thenReturn(orderList);

        assertTrue(validator.validateProductOrderListFromDatabase("Daniel", productsList));

    }
}