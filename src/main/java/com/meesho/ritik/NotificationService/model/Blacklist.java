package com.meesho.ritik.NotificationService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "blacklist")
@NoArgsConstructor
@Data
@AllArgsConstructor
//@RedisHash("Blacklist") -> @Entity and @RedisHash cannot be used on same class. If you want to use @RedisHash than make
//                           another similar class with name BlacklistRedis and annotate it with @RedisHash
public class Blacklist implements Serializable {

    @Id
    @Column(name = "phone_number")
    private String phoneNumber;
}
