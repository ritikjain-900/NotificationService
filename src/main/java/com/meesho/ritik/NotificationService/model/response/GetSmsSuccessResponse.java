package com.meesho.ritik.NotificationService.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetSmsSuccessResponse {

    private int id;

    private String phoneNumber;

    private String message;

    private String status;

    private String failureCode;

    private String failureComments;

    private Date createdAt;

    private Date updatedAt;
}
