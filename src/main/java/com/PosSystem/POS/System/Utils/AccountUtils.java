package com.PosSystem.POS.System.Utils;
import com.PosSystem.POS.System.Dao.UserRepo;
import com.PosSystem.POS.System.Dto.AccountInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;

public class AccountUtils {
    public static String generateEmployeeCode(UserRepo userRepo) {
        int min = 1000;
        int max = 9999;

        String employeeID;
        do {
            int randomNum = (int) (Math.floor(Math.random() * (max - min + 1)) + min);

            employeeID = String.valueOf(randomNum);
        } while (userRepo.existsByEmployeeCode(employeeID));

        return employeeID;
    }
}

