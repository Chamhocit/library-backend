package com.example.aptech.spring.library.dao;

import com.example.aptech.spring.library.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
    Checkout findByUserEmailAndBookId(String userEmail, Long bookId);

    List<Checkout> findByUserEmail(String userEmail);

    @Modifying
    @Query("delete from Checkout where bookId in :bookId")
    void deleteAllByBookId(@Param("bookId") Long bookId);
}
