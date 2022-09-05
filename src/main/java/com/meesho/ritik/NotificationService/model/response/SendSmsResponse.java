package com.meesho.ritik.NotificationService.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SendSmsResponse {

    private SendSmsSuccessResponse data;

    private SendSmsErrorResponse error;


}
