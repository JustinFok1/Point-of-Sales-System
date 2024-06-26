package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dao.UserRepo;
import com.PosSystem.POS.System.Dto.*;
import com.PosSystem.POS.System.Entity.Role;
import com.PosSystem.POS.System.Entity.User;
import com.PosSystem.POS.System.Security.JwtTokenProvider;
import com.PosSystem.POS.System.Utils.AccountUtils;
import com.PosSystem.POS.System.Utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailService emailService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Override
    public SystemResponse createAccount(UserRequest userRequest) {
        if (userRepo.existsByEmail(userRequest.getEmail())){
            return SystemResponse.builder()
                    .responseCode(Messages.EMAIL_EXISTS_CODE)
                    .responseMessage(Messages.EMAIL_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .address(userRequest.getAddress())
                .employeeCode(AccountUtils.generateEmployeeCode(userRepo))
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_USER"))
                .build();

        User savedUser = userRepo.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userRequest.getEmail())
                .subject("Welcome to ShareTea! Your Account Has Been Successfully Created")
                .messageBody("Welcome to the ShareTea family! We're excited to have you join us.\n" +
                        "\n" +
                        "Your account has been successfully created. Here are your account details:\n" +
                        "\n" +
                        "- Full Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + "\n" +
                        "- Email: "+savedUser.getEmail() + "\n" +
                        "- Employee Code: " + savedUser.getEmployeeCode() + "\n" +
                        "\n" +
                        "At ShareTea, we are passionate about delivering the best boba tea experience to our customers. With your new account, you can easily clock into work and access all readily available materials.\n" +
                        "\n" +
                        "If you have any questions or need assistance, feel free to reach out to us at support@sharetea.com or visit our FAQ page.\n" +
                        "\n" +
                        "Thank you for choosing ShareTea. We look forward to working with you!\n" +
                        "\n" +
                        "Best regards,\n" +
                        "\n" +
                        "The ShareTea Team\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "Follow us on social media:\n" +
                        "- Instagram: @ShareTea\n" +
                        "- Facebook: @ShareTea\n" +
                        "- Twitter: @ShareTea\n" +
                        "\n" +
                        "ShareTea | The Best Boba Tea in Town")
                .build();

        emailService.sendEmailAlert(emailDetails);

        return SystemResponse.builder()
                .responseCode(Messages.ACCOUNT_CREATED_CODE)
                .responseMessage(Messages.ACCOUNT_CREATED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .employeeNumber(savedUser.getEmployeeCode())
                        .build())
                .build();
    }

    public SystemResponse login(LoginDTO loginDTO){
        Authentication authentication;
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        EmailDetails loginAlert = EmailDetails.builder()
                .subject("You're logged in!")
                .recipient(loginDTO.getEmail())
                .messageBody("You logged into your account. If you did not initiate, please contact your bank.")
                .build();
        emailService.sendEmailAlert(loginAlert);

        Optional<User> currentUser = userRepo.findByEmail(loginDTO.getEmail());
        AccountInfo currentAccount = AccountInfo.builder()
                .accountName(currentUser.get().getFirstName() + " " + currentUser.get().getLastName())
                .employeeNumber(currentUser.get().getEmployeeCode())
                .build();
        return SystemResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .accountInfo(currentAccount)
                .build();
    }


}
