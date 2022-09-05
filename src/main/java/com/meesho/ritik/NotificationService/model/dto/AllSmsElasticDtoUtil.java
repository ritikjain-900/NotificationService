package com.meesho.ritik.NotificationService.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllSmsElasticDtoUtil {

    private String phoneNumber;

    private String message;
}
