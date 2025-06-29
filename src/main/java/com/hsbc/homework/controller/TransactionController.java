package com.hsbc.homework.controller;

import com.hsbc.homework.models.Transaction;
import com.hsbc.homework.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Transaction> getTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping(value = "/transactions/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Transaction> getAllTransactionsPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return transactionService.getAllTransactionsPageable(pageable);
    }

    @GetMapping(value = "/transaction/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Transaction getTransactionById(@PathVariable String name) {
        return transactionService.getTransactionByIName(name);
    }

    @GetMapping(value = "/transaction/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping("/transaction")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PutMapping("/transaction/{id}")
    public Transaction updateTransaction(
            @PathVariable Long id,
            @RequestBody Transaction updatedTransaction) {
        return transactionService.updateTransaction(id, updatedTransaction);
    }

    @DeleteMapping("/transaction/{id}")
    public Long deleteTransaction(@PathVariable Long id) {
        return transactionService.deleteTransaction(id);
    }
}
