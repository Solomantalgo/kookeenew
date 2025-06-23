package com.kookee.merchandiser_backend.repository;

import com.kookee.merchandiser_backend.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
