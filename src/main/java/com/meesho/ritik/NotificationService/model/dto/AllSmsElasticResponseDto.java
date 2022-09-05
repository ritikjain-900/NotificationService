package com.meesho.ritik.NotificationService.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllSmsElasticResponseDto {

    @JsonProperty("messages")
    Set<AllSmsElasticDtoUtil> data;

    @JsonProperty("has_next")
    Boolean hasNext;

    @JsonProperty("error")
    private String error;
}
