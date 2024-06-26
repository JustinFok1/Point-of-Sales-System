package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dto.AccountInfo;
import com.PosSystem.POS.System.Dto.LoginDTO;
import com.PosSystem.POS.System.Dto.SystemResponse;
import com.PosSystem.POS.System.Dto.UserRequest;

public interface UserService {
    SystemResponse createAccount(UserRequest userRequest);
    SystemResponse login(LoginDTO loginDTO);

}
