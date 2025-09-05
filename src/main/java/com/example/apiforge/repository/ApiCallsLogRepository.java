package com.example.apiforge.repository;

import com.example.apiforge.entity.ApiCallsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiCallsLogRepository extends JpaRepository<ApiCallsLog, Long> {
    List<ApiCallsLog> findByEndpointId(Long endpointId);
    List<ApiCallsLog> findByEndpointIdAndCalledAtBetween(Long endpointId, LocalDateTime start, LocalDateTime end);
}
