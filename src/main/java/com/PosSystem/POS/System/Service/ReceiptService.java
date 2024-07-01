package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Entity.Order;

import java.util.Optional;

public interface ReceiptService {
    void createReceipt(Optional<Order> currentOrder);
}
