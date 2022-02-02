package com.intershipjava.intershipproject.services;

import com.intershipjava.intershipproject.dto.*;
import com.intershipjava.intershipproject.model.Customer;
import com.intershipjava.intershipproject.model.Order;
import com.intershipjava.intershipproject.model.Product;

import javax.mail.internet.ParseException;
import java.util.List;

public interface OrdersService {

    List<OrderDto> getOrderListDba();

    OrderDto createOrderDBA(OrderCreateDto orderRequest);

    Order payOrderDba(OrderPayDto orderPayRequest) throws ParseException, java.text.ParseException;

    OrderDto orderToOrderDto(Order order);

    Order orderDtoToOrder(OrderDto orderDto);

    List<OrderDto> orderToOrderDto(List<Order> orderList);

    OrderDto createDtoOrder(Customer customer, List<String> idProducts);

    OrderDto getOrderByUuid(String uuid);

    boolean deleteOrderByUuid(String uuid);

    boolean addProductToExistingOrder(AddProductsToOrderRequest addProductsToOrderRequest);

    boolean removeProductFromExistingOrder(String OrderUuid, String productUuid, String Name);

    List<ProductDto> getShoppingCartProducts(String name, String email);

    String getShoppingCartOrderId(String name, String email);
}