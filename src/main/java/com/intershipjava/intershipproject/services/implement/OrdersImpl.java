package com.intershipjava.intershipproject.services.implement;

import com.intershipjava.intershipproject.constants.Constant;
import com.intershipjava.intershipproject.dto.*;
import com.intershipjava.intershipproject.exceptions.OrderExceptions;
import com.intershipjava.intershipproject.factory.AbstractOrderFactory;
import com.intershipjava.intershipproject.model.CreditCard;
import com.intershipjava.intershipproject.model.Customer;
import com.intershipjava.intershipproject.model.Order;
import com.intershipjava.intershipproject.model.Product;
import com.intershipjava.intershipproject.repository.OrderRepository;
import com.intershipjava.intershipproject.repository.ProductRepository;
import com.intershipjava.intershipproject.services.OrdersService;
import com.intershipjava.intershipproject.services.ProductService;
import com.intershipjava.intershipproject.services.validator.ValidatorService;
import jdk.nashorn.internal.ir.IfNode;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.ParseException;
import javax.print.attribute.standard.MediaSize;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrdersImpl implements OrdersService, Serializable {

    private final transient ValidatorService validatorService;
    private final transient OrderRepository orderRepository;
    private final transient ProductRepository productRepository;
    private final transient ProductService productService;

    @Override
    public List<OrderDto> getOrderListDba() {
        return orderToOrderDto(orderRepository.findAll());
    }

    @Override
    public List<ProductDto> getShoppingCartProducts(String name, String email){
        Order order = orderRepository.findOrderByNameAndStatus(name,false,email);
        if(order == null){
            return null;
        }else {
            return productService.productsToDto(order.getProductList());
        }
    }

    @Override
    public String getShoppingCartOrderId(String name, String email){
        if(orderRepository.findOrderByNameAndStatus(name,false,email) == null){
            return null;
        }else {
            Order order = orderRepository.findOrderByNameAndStatus(name,false,email);
            return order.getOrderId();
        }
    }


    @Override
    public OrderDto createOrderDBA(OrderCreateDto orderPayDto) {
        if (orderPayDto == null) {
            throw new OrderExceptions(Constant.errorMessages.INVALID_OBJECT);
        }
        Customer customer = orderPayDto.getCustomer();
        validatorService.isValidName(customer.getName());
        validatorService.isValidEmailAddress(customer.getEmail());

        validatorService.validateProductsId(orderPayDto.getIdProducts());
        validatorService.validateStock(orderPayDto.getIdProducts());
        validatorService.validateShoppingCartProducts(orderPayDto.getIdProducts());
        validatorService.validateProductOrderListFromDatabase(customer.getName(), orderPayDto.getIdProducts());


        return createDtoOrder(customer, orderPayDto.getIdProducts());
    }

    @Override
    public OrderDto getOrderByUuid(String uuid){
        return orderToOrderDto(orderRepository.findOrderById(uuid));
    }

    @Transactional
    @Override
    public Order payOrderDba(OrderPayDto orderPayRequest) throws ParseException, java.text.ParseException {
        List<Product> productList = orderRepository.findOrderById(orderPayRequest.getIdOrder()).getProductList();
        Order order = orderRepository.findOrderById(orderPayRequest.getIdOrder());
        CreditCard creditCard = orderPayRequest.getCreditCard();
        validatorService.validateCreditCardNumberLength(creditCard.getCreditCardNumber());
        validatorService.validateLuhnCreditCardAlgorithm(creditCard.getCreditCardNumber());
        validatorService.isValidCIV(creditCard.getCiv());
        validatorService.isValidName(creditCard.getCardHolderName());
        validatorService.isValidDate(creditCard.getExpiryDate());

        if (order == null) {
            throw new OrderExceptions(Constant.errorMessages.ORDER_NOT_EXIST + "Sure" + orderPayRequest.getIdOrder());
        }
        if (order.isPayed()) {
            throw new OrderExceptions(Constant.errorMessages.ORDER_ALREADY_PAYED);
        }
        for (Product product : productList){
            if (product.getQuantity() < 1){
                throw new OrderExceptions(Constant.errorMessages.PRODUCT_OUT_OF_STOCK);
            }
        }
             orderRepository.updateOrderStatus(order.getOrderId(), true);
             for (Product product : productList) {
              productRepository.updateProductQuantity(product.getUuid());
             }

        return order;
    }

    @Override
    public boolean deleteOrderByUuid(String uuid){
        Order order = orderRepository.findOrderById(uuid);
        if (order == null){
            return false;
        }
        orderRepository.delete(order);

        return  true;
    }

    @Override
    public boolean addProductToExistingOrder(AddProductsToOrderRequest addProductsToOrderRequest){
        List<String> idProducts = new ArrayList<>();
        //Check if exist an order
        Order order = orderRepository.findOrderByNameAndStatus(addProductsToOrderRequest.getName(),
                false,addProductsToOrderRequest.getEmail());
        if (order == null){
            //Create new order with received product
            Customer customer = new Customer();
            OrderCreateDto orderCreateDto = new OrderCreateDto();
            List<String> idProductsList = new ArrayList<>();

            idProductsList.add(addProductsToOrderRequest.getUuidProduct());
            customer.setName(addProductsToOrderRequest.getName());
            customer.setEmail(addProductsToOrderRequest.getEmail());

            orderCreateDto.setCustomer(customer);
            orderCreateDto.setIdProducts(idProductsList);
            createOrderDBA(orderCreateDto);

            return true;

        }else {

            //If exist we add the product
                OrderDto orderDto = orderToOrderDto(order);
                idProducts.add(addProductsToOrderRequest.getUuidProduct());

                ProductDto productDto = productService.productToDto(productService.getProductDba(addProductsToOrderRequest.getUuidProduct()));

                if(validatorService.validateProductOrderListFromDatabase(orderDto.getCustomerName(), idProducts)){
                    orderDto.getProductDtoList().add(productDto);
                    orderRepository.save(orderDtoToOrder(orderDto));
                    return true;
                }else {
                    throw new OrderExceptions(Constant.errorMessages.PRODUCT_EXIST);
                }

        }
    }

    @Override
    public boolean removeProductFromExistingOrder(String orderUuid, String productUuid, String name){

        OrderDto orderDto = getOrderByUuid(orderUuid);
        if (orderDto == null){
            throw new OrderExceptions("Invalid order uuid!");
        }
        List<ProductDto> productDtoList = orderDto.getProductDtoList();

        if (productDtoList.size() == 1 && productDtoList.get(0).getUuid().equals(productUuid)){
            deleteOrderByUuid(orderDto.getOrderId());
            return true;
        }
        for (ProductDto productDto: productDtoList){
            if (productDto.getUuid().equals(productUuid)) {
                productDtoList.remove(productDto);
                orderRepository.save(orderDtoToOrder(orderDto));
                return true;
            }
        }

        return false;
    }



    @Override
    public OrderDto createDtoOrder(Customer customer, List<String> idProducts) {
        UUID uuid2 = UUID.randomUUID();
        double totalAmount;
        List<ProductDto> productDtoList = productService.getProductListByIdDB(idProducts);
        totalAmount = productDtoList.stream().mapToDouble(ProductDto::getPrice).sum();

        OrderDto orderDto = new OrderDto
                .Builder(uuid2.toString())
                .withDate(LocalDateTime.now())
                .withCustomerName(customer.getName())
                .withListOfProduct(productDtoList)
                .withTotalAmount(totalAmount)
                .withCustomerEmail(customer.getEmail())
                .build();

        AbstractOrderFactory orderFactory = AbstractOrderFactory.getFactory(customer.getEmail().contains("endava"));
        OrderDto orderDto1 = orderFactory.getOrderDto(orderDto, false);

        orderRepository.save(orderDtoToOrder(orderDto1));
        return orderDto1;

    }



    @Override
    public OrderDto orderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderId(order.getOrderId());
        orderDto.setCustomerName(order.getCustomerName());
        orderDto.setPaid(order.isPayed());
        orderDto.setExternal(order.isExternal());
        orderDto.setCustomerEmail(order.getCustomerEmail());
        orderDto.setLocalDateTime(order.getLocalDateTime());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setProductDtoList(productService.productsToDto(order.getProductList()));

        return orderDto;
    }

    @Override
    public List<OrderDto> orderToOrderDto(List<Order> orderList) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order order : orderList) {
            orderDtoList.add(orderToOrderDto(order));
        }

        return orderDtoList;
    }

    @Override
    public Order orderDtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setOrderId(orderDto.getOrderId());
        order.setCustomerName(orderDto.getCustomerName());
        order.setCustomerEmail(orderDto.getCustomerEmail());
        order.setPayed(orderDto.isPaid());
        order.setExternal(orderDto.isExternal());
        order.setLocalDateTime(orderDto.getLocalDateTime());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setProductList(productService.productDtoToProduct(orderDto.getProductDtoList()));

        return order;
    }


}
