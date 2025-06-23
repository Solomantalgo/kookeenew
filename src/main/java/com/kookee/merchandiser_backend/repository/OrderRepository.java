package com.kookee.merchandiser_backend.repository;

import com.kookee.merchandiser_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
