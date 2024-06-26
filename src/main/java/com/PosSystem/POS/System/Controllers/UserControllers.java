package com.PosSystem.POS.System.Controllers;

import com.PosSystem.POS.System.Dao.UserRepo;
import com.PosSystem.POS.System.Dto.LoginDTO;
import com.PosSystem.POS.System.Dto.SystemResponse;
import com.PosSystem.POS.System.Dto.UserRequest;
import com.PosSystem.POS.System.Entity.User;
import com.PosSystem.POS.System.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControllers {
    @Autowired
    UserService userService;
    @Autowired
    UserRepo userRepo;

    @PostMapping("/create")
    SystemResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }
    @GetMapping("/listAllUsers")
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @DeleteMapping("/deleteAll")
    public void deleteAll(){
        userRepo.deleteAll();
    }
    @PostMapping("/login")
    public SystemResponse login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }
}
