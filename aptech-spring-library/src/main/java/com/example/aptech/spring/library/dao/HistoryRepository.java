package com.example.aptech.spring.library.dao;

import com.example.aptech.spring.library.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    Page<History> findHistoriesByUserEmail(@RequestParam("userEmail") String userEmail, Pageable pageable);
}
