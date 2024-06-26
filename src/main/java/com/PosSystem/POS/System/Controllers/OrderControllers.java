package com.PosSystem.POS.System.Controllers;

import com.PosSystem.POS.System.Dto.*;
import com.PosSystem.POS.System.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderControllers {
    @Autowired
    OrderService orderService;

    @PostMapping("/createNewOrder")
    OrderResponse createNewOrder(){
        return orderService.createNewOrder();
    }
    @PostMapping("/addToOrder")
    OrderResponse addToOrder(@RequestBody OrderItemInfoDTO orderItemInfoDTO){
        return orderService.addToOrder(orderItemInfoDTO);
    }
    @PostMapping("/checkout")
    public CheckoutResponse processPayment() {
        return orderService.checkout();
    }
    @PutMapping("/setCustomerInfo")
    OrderResponse setCustomerInfo(@RequestBody CustomerInfoDetails customerInfoDetails){
        return orderService.setCustomerInfo(customerInfoDetails);
    }
    @GetMapping("/getOrderByOrderNumberAndDate")
    OrderResponse getOrderByOrderNumber(@RequestBody OrderDetailByNumberAndDate order){
        return orderService.getOrderByOrderNumberAndDate(order);
    }
    @GetMapping("/getOrderByCustomerInfo")
    OrderResponse getOrderByCustomerInfo(CustomerInfoDetails customerInfoDetails){
        return orderService.getOrderByCustomerInfo(customerInfoDetails);
    }
    @DeleteMapping("/delete/{orderItemId}")
    OrderResponse deleteOrderItemById(@PathVariable Long orderItemId){
        return orderService.deleteOrderItemById(orderItemId);
    }
    @DeleteMapping("/deleteAllOrders")
    OrderResponse deleteAllOrders(){
        return orderService.deleteAllOrders();
    }
}
