package com.meesho.ritik.NotificationService.model.request.thirdPartyApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThirdPartyApiRequest {

    @JsonProperty("deliverychannel")
    private String deliveryChannel;

    private Channels channels;

    @JsonProperty("destination")
    private List<Destination> destinations;
}
