package com.example.aptech.spring.library.dao;

import com.example.aptech.spring.library.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    Page<Message> findMessagesByUserEmail(@RequestParam("userEmail") String userEmail, Pageable pageable);

    Page<Message> findMessagesByClosed(@RequestParam("closed") boolean closed, Pageable pageable);
}
