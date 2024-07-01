package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dao.*;
import com.PosSystem.POS.System.Dto.*;
import com.PosSystem.POS.System.Entity.*;
import com.PosSystem.POS.System.Utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    CategoriesRepo categoriesRepo;
    @Autowired
    DrinkRepos drinkRepos;
    @Autowired
    ToppingsRepo toppingsRepo;
    @Autowired
    OrderItemRepo orderItemRepo;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    StripeService stripeService;
    @Autowired
    ReceiptService receiptService;

    @Override
    public OrderResponse createNewOrder() {
        Integer orderNum = orderRepo.findMaxOrderNumber();

        Optional<Order> latestOrder = orderRepo.findLatestOrder();
        String latestOrderDate = latestOrder.map(order -> order.getOrderDate().toString()).orElse(null);
        if(orderNum == null || latestOrderDate == null || !latestOrderDate.equals(LocalDate.now().toString()) || orderNum > 999){
            orderNum = 0;
        }

        Order newOrder = Order.builder()
                .orderNumber(orderNum + 1)
                .totalPrice(BigDecimal.ZERO)
                .numberOfItems(0)
                .status(OrderStatus.PENDING)
                .orderDate(LocalDate.now())
                .build();
        orderRepo.save(newOrder);

        return OrderResponse.builder()
                .responseCode(Messages.CREATED_CODE)
                .responseMessage(Messages.CREATED_MESSAGE)
                .orderNumber(newOrder.getOrderNumber())
                .build();
    }
    public OrderResponse addToOrder(OrderItemInfoDTO orderItemInfoDTO){
        Optional<Order> currentOrder = orderRepo.findLatestOrder();
        Optional<Drinks> findDrink = drinkRepos.findByDrinkName(orderItemInfoDTO.getDrinkName());

        List<Toppings> listOfToppings = orderItemInfoDTO.getToppingNames().stream()
                .map(toppingName -> toppingsRepo.findByToppingName(toppingName).get())
                .collect(Collectors.toList());

        BigDecimal toppingsTotalPrice = listOfToppings.stream()
                .map(topping -> topping.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderItem newOrderItem = OrderItem.builder()
                .order(currentOrder.get())
                .drinks(findDrink.get())
                .drinkPrice(findDrink.get().getPrice())
                .toppings(listOfToppings)
                .toppingsPrice(toppingsTotalPrice)
                .itemPrice(findDrink.get().getPrice().add(toppingsTotalPrice))
                .build();

        int currentNumberOfItems = currentOrder.get().getNumberOfItems();
        currentOrder.get().setNumberOfItems(currentNumberOfItems + 1);

        BigDecimal currentTotal = currentOrder.get().getTotalPrice();
        currentOrder.get().setTotalPrice(currentTotal.add(newOrderItem.getItemPrice()));

        currentOrder.get().getOrderItems().add(newOrderItem);
        orderRepo.save(currentOrder.get());

       return OrderResponse.builder()
                .responseCode(Messages.ORDER_ITEM_ADDED_CODE)
                .responseMessage(Messages.ORDER_ITEM_ADDED_MESSAGE)
                .orderNumber(currentOrder.get().getOrderNumber())
                .build();
    }

    public OrderResponse setCustomerInfo(CustomerInfoDetails customerInfoDetails){
        Optional<Order> currentOrder = orderRepo.findLatestOrder();
        if(currentOrder.isPresent()){
            currentOrder.get().setCustomerName(customerInfoDetails.getCustomerName());
            currentOrder.get().setPhoneNumber(customerInfoDetails.getPhoneNumber());
            orderRepo.save(currentOrder.get());

            stripeService.createStripeCustomer();

            return OrderResponse.builder()
                    .responseCode(Messages.SUCCESS_CODE)
                    .responseMessage(Messages.SUCCESS_MESSAGE)
                    .orderNumber(currentOrder.get().getOrderNumber())
                    .build();
        }
        return OrderResponse.builder()
                .responseCode(Messages.FAIL_CODE)
                .responseMessage(Messages.FAIL_MESSAGE)
                .build();
    }

    public OrderResponse getOrderByCustomerInfo(CustomerInfoDetails customerInfoDetails){
        List<Order> findOrder = orderRepo.findByCustomerNameAndPhoneNumber(customerInfoDetails.getCustomerName(), customerInfoDetails.getPhoneNumber());

        if (findOrder.isEmpty()) {
            return OrderResponse.builder()
                    .responseCode(Messages.DOES_NOT_EXIST_CODE)
                    .responseMessage(Messages.DOES_NOT_EXIST_MESSAGE)
                    .build();
        }

        List<OrderDetails> customerItems = findOrder.stream()
                .flatMap(order -> order.getOrderItems().stream()
                        .map(item -> {
                            List<ToppingDetails> toppingDetails = item.getToppings().stream()
                                    .map(topping -> new ToppingDetails(topping.getToppingName(), topping.getPrice()))
                                    .collect(Collectors.toList());

                            return OrderDetails.builder()
                                    .orderNumber(item.getOrder().getOrderNumber())
                                    .totalPrice(item.getItemPrice())
                                    .orderItemInfoDetails(OrderItemInfoDetails.builder()
                                            .categoryName(item.getDrinks().getCategoryName())
                                            .drinkName(item.getDrinks().getDrinkName())
                                            .drinkPrice(item.getDrinkPrice())
                                            .toppings(toppingDetails)
                                            .build())
                                    .build();
                        })
                )
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .responseCode(Messages.SUCCESS_CODE)
                .responseMessage(Messages.SUCCESS_MESSAGE)
                .orderDetails(customerItems)
                .build();
    }
    public OrderResponse getOrderByOrderNumberAndDate(OrderDetailByNumberAndDateDTO orderDetails) {
        Optional<Order> foundOrder = orderRepo.findByOrderNumberAndOrderDate(orderDetails.getOrderNumber(), LocalDate.parse(orderDetails.getOrderDate()));

        if (foundOrder.isEmpty()) {
            return OrderResponse.builder()
                    .responseCode(Messages.DOES_NOT_EXIST_CODE)
                    .responseMessage(Messages.DOES_NOT_EXIST_MESSAGE)
                    .orderNumber(orderDetails.getOrderNumber())
                    .build();
        }

        List<OrderDetails> orderItemDetails = foundOrder.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(item -> {
                    List<ToppingDetails> toppingDetails = item.getToppings().stream()
                            .map(topping -> new ToppingDetails(topping.getToppingName(), topping.getPrice()))
                            .collect(Collectors.toList());

                    return OrderDetails.builder()
                            .orderNumber(item.getOrder().getOrderNumber())
                            .totalPrice(item.getItemPrice())
                            .orderItemInfoDetails(OrderItemInfoDetails.builder()
                                    .categoryName(item.getDrinks().getCategoryName())
                                    .drinkName(item.getDrinks().getDrinkName())
                                    .drinkPrice(item.getDrinkPrice())
                                    .toppings(toppingDetails)
                                    .build())
                            .build();
                })
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderNumber(orderDetails.getOrderNumber())
                .responseCode(Messages.SUCCESS_CODE)
                .responseMessage(Messages.SUCCESS_MESSAGE)
                .orderDetails(orderItemDetails)
                .build();
    }

    public OrderResponse deleteAllOrders() {
        List<Order> allOrders = orderRepo.findAll();

        if(allOrders.isEmpty()){
            return OrderResponse.builder()
                    .responseCode(Messages.EMPTY_CODE)
                    .responseMessage(Messages.EMPTY_MESSAGE)
                    .build();
        }
        orderRepo.deleteAll(allOrders);

        return OrderResponse.builder()
                .responseCode(Messages.DELETED_CODE)
                .responseMessage(Messages.DELETED_MESSAGE)
                .build();
    }
    public OrderResponse deleteOrderItemById(Long orderItemId) {
        Optional<OrderItem> itemToDelete = orderItemRepo.findById(orderItemId);

        if (itemToDelete.isPresent()) {
            Optional<Order> orderToDeleteParent = orderRepo.findByOrderItemsId(orderItemId);
            BigDecimal updateTotalPrice = orderToDeleteParent.get().getTotalPrice().subtract(itemToDelete.get().getItemPrice());
            orderToDeleteParent.get().setTotalPrice(updateTotalPrice);

            int updateNumberOfDrinks = orderToDeleteParent.get().getNumberOfItems() - 1;
            orderToDeleteParent.get().setNumberOfItems(updateNumberOfDrinks);
            orderRepo.save(orderToDeleteParent.get());

            OrderItem orderItem = itemToDelete.get();
            orderItem.getToppings().clear();
            orderItemRepo.delete(orderItem);

            return OrderResponse.builder()
                    .responseCode(Messages.DELETED_CODE)
                    .responseMessage(Messages.DELETED_MESSAGE)
                    .build();
        }
        return OrderResponse.builder()
                .responseCode(Messages.DOES_NOT_EXIST_CODE)
                .responseMessage(Messages.DOES_NOT_EXIST_MESSAGE)
                .build();
    }
    public CheckoutResponse checkout(){
        Optional<Order> currentOrder = orderRepo.findLatestOrder();
        if(currentOrder.isEmpty()){
            return CheckoutResponse.builder()
                    .responseCode(Messages.ORDER_IS_EMPTY_CODE)
                    .responseMessage(Messages.ORDER_IS_EMPTY_MESSAGE)
                    .build();
        }

        Long totalPriceInCents = currentOrder.get().getTotalPrice().longValue() * 100;
        stripeService.createStripePaymentIntent(totalPriceInCents);

        currentOrder.get().setStatus(OrderStatus.COMPLETED);
        orderRepo.save(currentOrder.get());

        receiptService.createReceipt(currentOrder);

        return CheckoutResponse.builder()
                .responseCode(Messages.SUCCESS_CODE)
                .responseMessage(Messages.SUCCESS_MESSAGE)
                .build();
    }
   /* private List<Order> findOrder(Optional<OrderDetailByNumberAndDate> orderDetailByNumberAndDate ,Optional<CustomerInfoDetails> customerInfoDetails){
        if(orderDetailByNumberAndDate.isPresent()) {
            List<Order> findOrder = orderRepo.findBy
        }
    }*/
}
