package com.intershipjava.intershipproject.services.implement;

import com.intershipjava.intershipproject.dto.OrderCreateDto;
import com.intershipjava.intershipproject.dto.OrderDto;
import com.intershipjava.intershipproject.dto.OrderPayDto;
import com.intershipjava.intershipproject.dto.ProductDto;
import com.intershipjava.intershipproject.exceptions.OrderExceptions;
import com.intershipjava.intershipproject.factory.AbstractOrderFactory;
import com.intershipjava.intershipproject.model.CreditCard;
import com.intershipjava.intershipproject.model.Customer;
import com.intershipjava.intershipproject.model.Order;
import com.intershipjava.intershipproject.model.Product;
import com.intershipjava.intershipproject.repository.OrderRepository;
import com.intershipjava.intershipproject.repository.ProductRepository;
import com.intershipjava.intershipproject.services.validator.ValidatorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.internet.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrdersImplTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductServiceImpl productService;
    @Mock
    AbstractOrderFactory orderFactory;
    @Mock
    private ValidatorService validator;
    @InjectMocks
    private OrdersImpl orderImplement;


    @Test
    void getOrderListDba() {
        List<Order> orderList = new ArrayList<>();
        Product product = new Product();
        product.setCategory("Computer");
        List<Product> productList = new ArrayList<>();
        productList.add(product);


        Order order = new Order();
        order.setId(1);
        order.setOrderId("1432112");
        order.setCustomerName("Andrei");
        order.setPayed(true);
        order.setExternal(true);
        order.setLocalDateTime(LocalDateTime.now());
        order.setTotalAmount(100);
        order.setProductList(productList);
        orderList.add(order);

        when(orderRepository.findAll()).thenReturn(orderList);
        List<OrderDto> orderDtoList = orderImplement.getOrderListDba();

        assertTrue(orderDtoList.get(0).getTotalAmount() == orderList.get(0).getTotalAmount()
                && orderDtoList.get(0).isExternal() == orderList.get(0).isExternal()
                && orderDtoList.get(0).isPaid() == orderList.get(0).isPayed()
                && orderDtoList.get(0).getCustomerName().equals(orderList.get(0).getCustomerName())
                && orderDtoList.get(0).getOrderId().equals(orderList.get(0).getOrderId())
                && orderDtoList.get(0).getId() == orderList.get(0).getId());
    }

    @Test
    void processOrderDBA_throwException() {
        assertThrows(OrderExceptions.class, () -> {
            orderImplement.createOrderDBA(null);
        });
    }

    @Test
    void processOrderDBA_true() {

        Customer customer = new Customer();
        customer.setEmail("sorin@yaoo.com");
        customer.setName("sorin");
        List<String> productList = new ArrayList<>();
        productList.add("453fdsf543asd");
        OrderCreateDto orderCreateDto = new OrderCreateDto().toBuilder().customer(customer).idProducts(productList).build();


        when(validator.isValidName("sorin")).thenReturn(true);
        when(validator.isValidEmailAddress("sorin@yaoo.com")).thenReturn(true);
        when(validator.validateProductsId(productList)).thenReturn(true);
        when(validator.validateStock(productList)).thenReturn(true);
        when(validator.validateShoppingCartProducts(productList)).thenReturn(true);


      //  assertTrue(orderImplement.processOrderDBA(orderCreateDto).isEmpty());
    }

    @Test
    void processOrderDBA_NullException() {
        assertThrows(OrderExceptions.class, () -> {
            orderImplement.createOrderDBA(null);
        });
    }

    @Test
    void payOrderDba_true() throws ParseException, java.text.ParseException {
        Product product = new Product();
        product.setQuantity(5);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Order order = new Order();
        order.setPayed(false);
        order.setOrderId("543543");
        order.setProductList(productList);
        OrderPayDto orderPayDto = new OrderPayDto();
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardNumber("414069533123543123");
        creditCard.setCardHolderName("Sorin");
        creditCard.setCiv("4240");
        creditCard.setExpiryDate("11/22");
        orderPayDto.setIdOrder("453543zx7dsf435");
        orderPayDto.setCreditCard(creditCard);
        when(validator.validateCreditCardNumberLength(anyString())).thenReturn(true);
        when(validator.validateLuhnCreditCardAlgorithm(anyString())).thenReturn(true);
        when(validator.isValidCIV(anyString())).thenReturn(true);
        when(validator.isValidName(anyString())).thenReturn(true);
        when(validator.isValidDate(anyString())).thenReturn(true);
        when(orderRepository.findOrderById(anyString())).thenReturn(order);
        assertEquals(order.getOrderId(), orderImplement.payOrderDba(orderPayDto).getOrderId());

    }

    @Test
    void payOrderDba_throwExceptionDate() throws ParseException, java.text.ParseException {
        Product product = new Product();
        product.setQuantity(5);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Order order = new Order();
        order.setPayed(false);
        order.setOrderId("543543");
        order.setProductList(productList);
        OrderPayDto orderPayDto = new OrderPayDto();
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardNumber("414069533123543123");
        creditCard.setCardHolderName("Sorin");
        creditCard.setCiv("4240");
        creditCard.setExpiryDate("11/22");
        orderPayDto.setIdOrder("453543zx7dsf435");
        orderPayDto.setCreditCard(creditCard);
        when(validator.validateCreditCardNumberLength(anyString())).thenReturn(true);
        when(validator.validateLuhnCreditCardAlgorithm(anyString())).thenReturn(true);
        when(validator.isValidCIV(anyString())).thenReturn(true);
        when(validator.isValidName(anyString())).thenReturn(true);
        when(validator.isValidDate(anyString())).thenReturn(false);
        assertThrows(OrderExceptions.class, () -> {
            orderImplement.payOrderDba(orderPayDto);
        });
    }

    @Test
    void payOrderDba_throwExceptionOrderNull() throws ParseException, java.text.ParseException {
        Product product = new Product();
        product.setQuantity(5);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Order order = new Order();
        order.setPayed(false);
        order.setOrderId("543543");
        order.setProductList(productList);
        OrderPayDto orderPayDto = new OrderPayDto();
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardNumber("414069533123543123");
        creditCard.setCardHolderName("Sorin");
        creditCard.setCiv("4240");
        creditCard.setExpiryDate("11/22");
        orderPayDto.setIdOrder("453543zx7dsf435");
        orderPayDto.setCreditCard(creditCard);
        when(validator.validateCreditCardNumberLength(anyString())).thenReturn(true);
        when(validator.validateLuhnCreditCardAlgorithm(anyString())).thenReturn(true);
        when(validator.isValidCIV(anyString())).thenReturn(true);
        when(validator.isValidName(anyString())).thenReturn(true);
        when(validator.isValidDate(anyString())).thenReturn(true);
        when(orderRepository.findOrderById(anyString())).thenReturn(null);
        assertThrows(OrderExceptions.class, () -> {
            orderImplement.payOrderDba(orderPayDto);
        });
    }

    @Test
    void payOrderDba_throwExceptionOrderStockProduct() throws ParseException, java.text.ParseException {
        Product product = new Product();
        product.setQuantity(0);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Order order = new Order();
        order.setPayed(false);
        order.setOrderId("543543");
        order.setProductList(productList);
        OrderPayDto orderPayDto = new OrderPayDto();
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardNumber("414069533123543123");
        creditCard.setCardHolderName("Sorin");
        creditCard.setCiv("4240");
        creditCard.setExpiryDate("11/22");
        orderPayDto.setIdOrder("453543zx7dsf435");
        orderPayDto.setCreditCard(creditCard);
        when(validator.validateCreditCardNumberLength(anyString())).thenReturn(true);
        when(validator.validateLuhnCreditCardAlgorithm(anyString())).thenReturn(true);
        when(validator.isValidCIV(anyString())).thenReturn(true);
        when(validator.isValidName(anyString())).thenReturn(true);
        when(validator.isValidDate(anyString())).thenReturn(true);
        when(orderRepository.findOrderById(anyString())).thenReturn(order);
        assertThrows(OrderExceptions.class, () -> {
            orderImplement.payOrderDba(orderPayDto);
        });
    }


    @Test
    void payOrderDba_throwExceptionOrderPayed() throws ParseException, java.text.ParseException {
        Product product = new Product();
        product.setQuantity(5);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Order order = new Order();
        order.setPayed(true);
        order.setOrderId("543543");
        order.setProductList(productList);
        OrderPayDto orderPayDto = new OrderPayDto();
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardNumber("414069533123543123");
        creditCard.setCardHolderName("Sorin");
        creditCard.setCiv("4240");
        creditCard.setExpiryDate("11/22");
        orderPayDto.setIdOrder("453543zx7dsf435");
        orderPayDto.setCreditCard(creditCard);
        when(validator.validateCreditCardNumberLength(anyString())).thenReturn(true);
        when(validator.validateLuhnCreditCardAlgorithm(anyString())).thenReturn(true);
        when(validator.isValidCIV(anyString())).thenReturn(true);
        when(validator.isValidName(anyString())).thenReturn(true);
        when(validator.isValidDate(anyString())).thenReturn(true);
        when(orderRepository.findOrderById(anyString())).thenReturn(order);
        assertThrows(OrderExceptions.class, () -> {
            orderImplement.payOrderDba(orderPayDto);
        });
    }

    @Test
    void createDtoOrder_Successfully() {
        List<Product> productList = new ArrayList<>();
        ProductDto productDto = new ProductDto();
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(productDto);
        Customer customer = new Customer();
        customer.setName("Sorin");
        customer.setEmail("sorin@endava.com");
        List<String> idProducts = new ArrayList<>();
        idProducts.add("firstString");

        OrderDto orderDto = new OrderDto
                .Builder("435dfsd435")
                .withDate(LocalDateTime.now())
                .withCustomerName("Marius")
                .withListOfProduct(productDtoList)
                .withTotalAmount(550)
                .build();
        when(productService.getProductListByIdDB(idProducts)).thenReturn(productDtoList);
        when(productService.productDtoToProduct(productDtoList)).thenReturn(productList);

        assertEquals(productList, orderImplement.createDtoOrder(customer, idProducts));
    }

    @Test
    void orderToOrderDto_true() {
        Product product = new Product();
        product.setCategory("Computer");
        List<Product> productList = new ArrayList<>();
        productList.add(product);


        Order order = new Order();
        order.setId(1);
        order.setOrderId("1432112");
        order.setCustomerName("Andrei");
        order.setPayed(true);
        order.setExternal(true);
        order.setLocalDateTime(LocalDateTime.now());
        order.setTotalAmount(100);
        order.setProductList(productList);


        when(productService.productDtoToProduct(anyList())).thenReturn(productList);
        OrderDto orderDto = orderImplement.orderToOrderDto(order);

        assertTrue(orderDto.getTotalAmount() == order.getTotalAmount()
                && orderDto.isExternal() == order.isExternal()
                && orderDto.isPaid() == order.isPayed()
                && orderDto.getCustomerName().equals(order.getCustomerName())
                && orderDto.getOrderId().equals(order.getOrderId())
                && orderDto.getId() == order.getId());
    }

    @Test
    void testListOrderToOrderDto_true() {
        List<Order> orderList = new ArrayList<>();
        Product product = new Product();
        product.setCategory("Computer");
        List<Product> productList = new ArrayList<>();
        productList.add(product);


        Order order = new Order();
        order.setId(1);
        order.setOrderId("1432112");
        order.setCustomerName("Andrei");
        order.setPayed(true);
        order.setExternal(true);
        order.setLocalDateTime(LocalDateTime.now());
        order.setTotalAmount(100);
        order.setProductList(productList);
        orderList.add(order);

        List<OrderDto> orderDtoList = orderImplement.orderToOrderDto(orderList);

        assertTrue(orderDtoList.get(0).getTotalAmount() == order.getTotalAmount()
                && orderDtoList.get(0).isExternal() == order.isExternal()
                && orderDtoList.get(0).isPaid() == order.isPayed()
                && orderDtoList.get(0).getCustomerName().equals(order.getCustomerName())
                && orderDtoList.get(0).getOrderId().equals(order.getOrderId())
                && orderDtoList.get(0).getId() == order.getId());
    }

    @Test
    void orderDtoToOrder_true() {
        ProductDto productDto = new ProductDto();
        productDto.setCategory("Computer");
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(productDto);

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setOrderId("1432112");
        orderDto.setCustomerName("Andrei");
        orderDto.setPaid(true);
        orderDto.setExternal(true);
        orderDto.setLocalDateTime(LocalDateTime.now());
        orderDto.setTotalAmount(100);
        orderDto.setProductDtoList(productDtoList);


        Order order = orderImplement.orderDtoToOrder(orderDto);
        when(productService.productsToDto(anyList())).thenReturn(productDtoList);

        assertTrue(orderDto.getTotalAmount() == order.getTotalAmount()
                && orderDto.isExternal() == order.isExternal()
                && orderDto.isPaid() == order.isPayed()
                && orderDto.getCustomerName().equals(order.getCustomerName())
                && orderDto.getOrderId().equals(order.getOrderId())
                && orderDto.getId() == order.getId());

    }
}