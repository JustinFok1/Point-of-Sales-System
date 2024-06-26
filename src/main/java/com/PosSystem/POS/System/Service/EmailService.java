package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
