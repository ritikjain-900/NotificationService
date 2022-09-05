package com.meesho.ritik.NotificationService.model.request.thirdPartyApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sms {

    private String text;
}
