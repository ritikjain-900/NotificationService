package com.meesho.ritik.NotificationService.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendSmsRequestBody {

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String message;
}
