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
public class Destination {

    private List<String> msisdn;

    @JsonProperty("correlationid")
    private String correlationId;
}
