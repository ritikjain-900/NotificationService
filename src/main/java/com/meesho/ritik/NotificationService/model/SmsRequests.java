package com.meesho.ritik.NotificationService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sms_requests")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class SmsRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private String status;

    @Column(name = "failure_code")
    private String failureCode;

    @Column(name = "failure_comments")
    private String failureComments;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @Override
    public String toString() {
        return "SmsRequests{" +
                "id=" + id +
                ", phone_number='" + phoneNumber + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", failure_code='" + failureCode + '\'' +
                ", failure_comments='" + failureComments + '\'' +
                ", created_at=" + createdAt +
                ", updated_at=" + updatedAt +
                '}';
    }

}
