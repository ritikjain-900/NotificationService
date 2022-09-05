package com.meesho.ritik.NotificationService.model.dto;

import com.meesho.ritik.NotificationService.model.response.GetSmsSuccessResponse;
import com.meesho.ritik.NotificationService.model.response.SendSmsErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetSmsResponseDto {

    private GetSmsSuccessResponse data;

    private SendSmsErrorResponse error;

}
