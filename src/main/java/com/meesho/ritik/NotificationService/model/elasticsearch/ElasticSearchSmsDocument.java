package com.meesho.ritik.NotificationService.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@Document(indexName = "sms_index")
@NoArgsConstructor
@AllArgsConstructor
public class ElasticSearchSmsDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text,name = "phone_number")
    private String phoneNumber;

    @Field(type =  FieldType.Text)
    @JsonProperty("message")
    private String message;

    @JsonProperty("created_at")
    @Field(type = FieldType.Date)
    private Date createdAt;
}
