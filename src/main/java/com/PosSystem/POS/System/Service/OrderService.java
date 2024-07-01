package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dto.*;


public interface OrderService {
    OrderResponse createNewOrder();
    OrderResponse addToOrder(OrderItemInfoDTO orderItemInfoDTO);
    OrderResponse setCustomerInfo(CustomerInfoDetails customerInfoDetails);
    OrderResponse getOrderByOrderNumberAndDate(OrderDetailByNumberAndDateDTO orderDetails);
    OrderResponse deleteOrderItemById(Long orderItemId);
    OrderResponse deleteAllOrders();
    OrderResponse getOrderByCustomerInfo(CustomerInfoDetails customerInfoDetails);
    CheckoutResponse checkout();
}
