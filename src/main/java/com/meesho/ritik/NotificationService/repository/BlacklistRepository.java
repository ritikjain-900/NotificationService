package com.meesho.ritik.NotificationService.repository;

import com.meesho.ritik.NotificationService.model.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, String> {
}
