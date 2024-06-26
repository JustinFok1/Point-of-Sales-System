package com.PosSystem.POS.System.Service;

public interface StripeService {
    void createStripeCustomer();
    String createStripePaymentIntent(Long amountInCents);
}
