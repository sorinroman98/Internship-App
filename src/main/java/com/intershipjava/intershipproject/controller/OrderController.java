package com.intershipjava.intershipproject.controller;

import com.intershipjava.intershipproject.dto.*;
import com.intershipjava.intershipproject.exceptions.ProductExceptions;
import com.intershipjava.intershipproject.model.Order;
import com.intershipjava.intershipproject.services.OrdersService;
import com.intershipjava.intershipproject.services.ProductService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.ParseException;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrdersService ordersService;

    @Autowired
    public OrderController(OrdersService ordersService, ProductService productService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/getOrdersDba")
    public List<OrderDto> returnOrderListDba() {
        return ordersService.getOrderListDba();
    }

    @PostMapping("/processOrderDBA")
    public ResponseEntity<OrderDto> insertOrderDba(@RequestBody OrderCreateDto orderRequest) throws ProductExceptions {

        OrderDto orderDto = ordersService.createOrderDBA(orderRequest);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PostMapping("/payOrderDBA")
    public ResponseEntity<OrderDto> payOrderDba(@RequestBody OrderPayDto orderPayRequest) throws ProductExceptions, ParseException, java.text.ParseException {
        Order order = ordersService.payOrderDba(orderPayRequest);
        return new ResponseEntity<>(ordersService.orderToOrderDto(order), HttpStatus.OK);
    }

    @GetMapping("/getOrderById")
    public OrderDto getOrderByIdDba(@RequestParam String uuid) {
        return ordersService.getOrderByUuid(uuid);
    }

    @GetMapping("/deleteOrderByUuid")
    public ResponseEntity<String> deleteOrderByUuid(@RequestParam String uuid) {

        if (ordersService.deleteOrderByUuid(uuid)){
            return new ResponseEntity<>(JSONObject.quote("Successfully"), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Unable to delete!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/addProductToOrder")
    public ResponseEntity<String> addProductToExistingOrder(@RequestBody AddProductsToOrderRequest addProductsToOrderRequest){

        ordersService.addProductToExistingOrder(addProductsToOrderRequest);
        return new ResponseEntity<>(JSONObject.quote("Successfully added!"), HttpStatus.OK);
    }

    @GetMapping("/removeProductFromOrder")
    public ResponseEntity<String> removeProductFromExistingOrder(@RequestParam String uuidProduct, @RequestParam String uuidOrder, @RequestParam String name){
        ordersService.removeProductFromExistingOrder(uuidOrder,uuidProduct,name);
        return new ResponseEntity<>(JSONObject.quote("Successfully removed!"), HttpStatus.OK);
    }

    @GetMapping("/getShoppingCartProducts")
    public ResponseEntity<List<ProductDto>> getShoppingCartProducts(@RequestParam String name, @RequestParam String email) {

        if (ordersService.getShoppingCartProducts(name,email) != null){

            return new ResponseEntity<>(ordersService.getShoppingCartProducts(name,email), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getShoppingCartOrderId")
    public ResponseEntity<String> getShoppingCardOrderId(@RequestParam String name, @RequestParam String email) {

        if (ordersService.getShoppingCartOrderId(name,email) != null){

            return new ResponseEntity<>(JSONObject.valueToString(ordersService.getShoppingCartOrderId(name,email)), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
