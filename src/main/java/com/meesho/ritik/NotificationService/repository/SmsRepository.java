package com.meesho.ritik.NotificationService.repository;

import com.meesho.ritik.NotificationService.model.SmsRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepository extends JpaRepository<SmsRequests, Integer> {

}
