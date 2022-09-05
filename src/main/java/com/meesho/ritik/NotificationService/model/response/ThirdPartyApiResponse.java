package com.meesho.ritik.NotificationService.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThirdPartyApiResponse {

    @JsonProperty("response")
    private List<ThirdPartyApiResponseObject> thirdPartyApiResponseObjectList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ThirdPartyApiResponseObject {


        @JsonProperty("code")
        private String code;

        @JsonProperty("transid")
        private String transId;

        @JsonProperty("description")
        private String description;

        @JsonProperty("correlationid")
        private String correlationId;


    }

    //private String error;
}
