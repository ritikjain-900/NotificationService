package com.meesho.ritik.NotificationService.model.request.thirdPartyApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Channels {

    private Sms sms;
}
