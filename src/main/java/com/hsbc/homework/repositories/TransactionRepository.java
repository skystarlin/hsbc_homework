package com.hsbc.homework.repositories;

import com.hsbc.homework.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "select * from transaction where name = ?1", nativeQuery = true)
    Transaction findByName(String name);
}
