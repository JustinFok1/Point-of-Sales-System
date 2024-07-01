package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dao.OrderRepo;
import com.PosSystem.POS.System.Dto.OrderDetails;
import com.PosSystem.POS.System.Dto.OrderItemInfoDetails;
import com.PosSystem.POS.System.Dto.ToppingDetails;
import com.PosSystem.POS.System.Entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    private static List<OrderDetails> allOrderDetails = new ArrayList<>();

    @Override
    public void createReceipt(Optional<Order> currentOrder) {
        if (currentOrder.isEmpty()) {
            return;
        }

        Order order = currentOrder.get();
        List<OrderDetails> orderItemDetails = order.getOrderItems().stream()
                .map(item -> {
                    List<ToppingDetails> toppingDetails = item.getToppings().stream()
                            .map(topping -> new ToppingDetails(topping.getToppingName(), topping.getPrice()))
                            .collect(Collectors.toList());

                    return OrderDetails.builder()
                            .orderNumber(item.getOrder().getOrderNumber())
                            .totalPrice(item.getItemPrice())
                            .date(item.getOrder().getOrderDate().toString())
                            .orderItemInfoDetails(OrderItemInfoDetails.builder()
                                    .categoryName(item.getDrinks().getCategoryName())
                                    .drinkName(item.getDrinks().getDrinkName())
                                    .drinkPrice(item.getDrinkPrice())
                                    .toppings(toppingDetails)
                                    .build())
                            .build();
                })
                .collect(Collectors.toList());

        String directoryPath = "src/Receipts";
        String fileName = order.getOrderNumber() + "_" + order.getOrderDate() + "_receipt.txt";
        String filePath = directoryPath + "/" + fileName;

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            BigDecimal totalAmount = BigDecimal.valueOf(0);

            writer.write("-------------------------------------\n");
            writer.write("         ShareTea        \n");
            writer.write("   No 00000 Address Line One \n");
            writer.write("   ShareTea.com \n");
            writer.write("        +9250000000      \n");
            writer.write("-------------------------------------\n");

            writer.write(String.format("%-30s %10s\n", "Item Name", "Price"));
            writer.write("-------------------------------------\n");

            for (OrderDetails orderDetails : orderItemDetails) {
                writer.write(String.format("%-25s %10s\n", orderDetails.getOrderItemInfoDetails().getDrinkName(), orderDetails.getOrderItemInfoDetails().getDrinkPrice()));
                for (ToppingDetails topping : orderDetails.getOrderItemInfoDetails().getToppings()) {
                    writer.write(String.format("          %-15s %10s\n", topping.getToppingName(), topping.getPrice()));
                }
                writer.write(String.format("      %-25s %10s\n", "Total", orderDetails.getTotalPrice()));
                totalAmount = totalAmount.add(orderDetails.getTotalPrice());
            }

            writer.write("-------------------------------------\n");
            writer.write(String.format(" Total amount:                  %10s\n", totalAmount));

            writer.write("*************************************\n");
            writer.write("       THANK YOU COME AGAIN            \n");
            writer.write("*************************************\n");
            writer.write("       ShareTea          \n");
            writer.write("   CONTACT: ShareTea@gmail.com       \n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
