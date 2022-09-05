package com.meesho.ritik.NotificationService.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistRequestBody {

    @JsonProperty("phone_numbers")
    private List<String> phoneNumbers;
}
