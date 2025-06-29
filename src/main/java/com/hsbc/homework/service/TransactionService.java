package com.hsbc.homework.service;

import com.hsbc.homework.exceptions.TransactionException;
import com.hsbc.homework.models.Transaction;
import com.hsbc.homework.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "transaction")
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    @Autowired
    private TransactionRepository transactionRepository;

    public Page<Transaction> getAllTransactionsPageable(Pageable pageable) {
        log.info("Get all transactions pageable");
        return transactionRepository.findAll(pageable);
    }

    public List<Transaction> getAllTransactions() {
        log.info("Get all transactions");
        return transactionRepository.findAll();
    }

    @Cacheable(key = "#id")
    public Transaction getTransactionById(Long id) {
        log.info("Get transaction by id: {}", id);
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.orElse(null);
    }

    @Cacheable(key = "#name")
    public Transaction getTransactionByIName(String name) {
        log.info("Get transaction by name: {}", name);
        return transactionRepository.findByName(name);
    }

    @CachePut(key = "#id")
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) throws TransactionException {
        log.info("Update transaction by id: {}", id);
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new TransactionException(HttpStatus.NOT_FOUND, "TRANSACTION_NOT_FOUND", String.format("Id %s not present!", id));
        }
        updatedTransaction.setId(id);
        transactionRepository.save(updatedTransaction);
        return getTransactionById(id);
    }

    @CacheEvict(key = "#id")
    public Long deleteTransaction(Long id) throws TransactionException {
        log.info("Delete transaction by id: {}", id);
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new TransactionException(HttpStatus.NOT_FOUND, "TRANSACTION_NOT_FOUND", String.format("Id %s not present!", id));
        }
        transactionRepository.deleteById(id);
        return id;
    }

    public Transaction createTransaction(Transaction transaction) throws TransactionException {
        log.info("Create transaction.");
        Transaction transactionSaved;
        try {
            transactionSaved = transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new TransactionException("Error Create transaction.", e);
        }
        return transactionSaved;
    }

}
