package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dao.OrderRepo;
import com.PosSystem.POS.System.Entity.Order;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StripeServiceImpl implements StripeService{
    @Autowired
    OrderRepo orderRepo;

    public void createStripeCustomer(){
        Optional<Order> currentOrder = orderRepo.findLatestOrder();

        if(currentOrder.isEmpty()){
            System.out.println("Order is empty");
        }

        Order order = currentOrder.get();
        Map<String, Object> params = new HashMap<>();
        params.put("name", order.getOrderNumber() + " | " + order.getOrderDate());
        params.put("phone", order.getPhoneNumber());

        try{
            Customer customer = Customer.create(params);

            System.out.println("Created Stripe customer");
        }catch (StripeException e){
           e.getMessage();
        }
    }
    public String createStripePaymentIntent(Long amountInCents){
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("usd")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            System.out.println("Created payment intent");

            String stripeId = paymentIntent.getId();
            System.out.println("Stripe ID: " + stripeId);
            return stripeId;
        }catch (StripeException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to create Payment Intent" + e.getMessage());
        }
    }
}
