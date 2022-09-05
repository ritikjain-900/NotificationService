package com.meesho.ritik.NotificationService.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class SendSmsErrorResponse {

    private String code;

    private String message;

    public SendSmsErrorResponse(){
        code = "INVALID_REQUEST";
        message = "Phone number is mandatory";
    }

    public SendSmsErrorResponse(String error){
        code = "INVALID_REQUEST";
        message = error;
    }
}
