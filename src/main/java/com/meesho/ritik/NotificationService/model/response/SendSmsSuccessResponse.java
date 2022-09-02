package com.meesho.ritik.NotificationService.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SendSmsSuccessResponse {

    private int requestId;

    private String comments;
}
