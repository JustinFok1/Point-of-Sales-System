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
        List<OrderDetails> orderItemDetails = currentOrder.stream()
                .flatMap(order -> order.getOrderItems().stream())
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

        allOrderDetails.addAll(orderItemDetails);

        saveReceiptToFile(orderItemDetails);
    }

    private void saveReceiptToFile(List<OrderDetails> orderItemDetails) {
        // Define the directory and file name
        String directoryPath = "src/Receipts"; // Relative path to the Receipts directory
        String fileName = allOrderDetails.get(0).getOrderNumber() + "_" + allOrderDetails.get(0).getDate() + "_receipt.txt";
        String filePath = directoryPath + "/" + fileName;

        // Create the directory if it doesn't exist
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

            writer.write(" Item Name                  Price   \n");
            writer.write("-------------------------------------\n");

            for (OrderDetails orderDetails : orderItemDetails) {
                writer.write(" " + orderDetails.getOrderItemInfoDetails().getDrinkName() + "                            \n");
                writer.write("      Toppings:\n");
                for (ToppingDetails topping : orderDetails.getOrderItemInfoDetails().getToppings()) {
                    writer.write("          " + topping.getToppingName() + " (" + topping.getPrice() + ")\n");
                }
                writer.write("      " + orderDetails.getTotalPrice() + "\n");
                totalAmount = totalAmount.add(orderDetails.getTotalPrice());
            }

            writer.write("-------------------------------------\n");
            writer.write(" Total amount:               " + totalAmount + "   \n");

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
