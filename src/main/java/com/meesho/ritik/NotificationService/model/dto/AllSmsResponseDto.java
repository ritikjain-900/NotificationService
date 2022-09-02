package com.meesho.ritik.NotificationService.model.dto;

import com.meesho.ritik.NotificationService.model.SmsRequests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllSmsResponseDto {

    private List<SmsRequests> data;

}
