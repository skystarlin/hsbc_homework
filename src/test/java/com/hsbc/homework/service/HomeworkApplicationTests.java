package com.hsbc.homework.service;

import com.hsbc.homework.exceptions.TransactionException;
import com.hsbc.homework.models.Transaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class HomeworkApplicationTests {

    @Autowired
    private TransactionService transactionService;

    @Test
    @Order(1)
    public void testGetAllTransactions() {
        createTransactions();
        List<Transaction> list = transactionService.getAllTransactions();
        Assertions.assertEquals(2, list.size());
    }

    @Test
    @Order(2)
    public void testGetTransactionById() {
        Transaction transaction = transactionService.getTransactionById(1L);
        Assertions.assertEquals("t-1", transaction.getName());
    }

    @Test
    @Order(3)
    public void testGetTransactionByName() {
        Transaction transaction = transactionService.getTransactionByIName("t-2");
        Assertions.assertEquals("sell", transaction.getType());
    }

    @Test
    @Order(4)
    public void testUpdateTransaction() {
        Transaction updateTransaction = new Transaction();
        updateTransaction.setName("t-update");
        updateTransaction.setType("buy");
        updateTransaction.setAmount(new BigDecimal("135.7"));
        Transaction transaction = transactionService.updateTransaction(1L, updateTransaction);
        Assertions.assertEquals("t-update", transaction.getName());
    }

    @Test
    @Order(5)
    public void testUpdateTransactionWithException() {
        Transaction updateTransaction = new Transaction();
        updateTransaction.setName("t-update");
        updateTransaction.setType("buy");
        updateTransaction.setAmount(new BigDecimal("135.7"));
        assertThrows(TransactionException.class, () -> transactionService.updateTransaction(3L, updateTransaction));
    }

    @Test
    @Order(6)
    public void testDeleteTransaction() {
        transactionService.deleteTransaction(1L);
        Transaction result = transactionService.getTransactionById(1L);
        Assertions.assertNull(result);
    }

    @Test
    @Order(7)
    public void testDeleteTransactionWithException() {
        assertThrows(TransactionException.class, () -> transactionService.deleteTransaction(1L));
    }

    @Test
    @Order(8)
    public void testGetAllTransactionsPageable() {
        create11Transaction();
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String direction = "asc";
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Transaction> pageableTransactions = transactionService.getAllTransactionsPageable(pageable);
        Assertions.assertEquals(12, pageableTransactions.getTotalElements());
    }

    public void create11Transaction(){
        for (int i = 0; i < 11; i++) {
            Transaction transaction = new Transaction();
            transaction.setName("t-" + i);
            if (i % 2 == 0) {
                transaction.setType("Sell");
            } else {
                transaction.setType("Buy");
            }
            double randomValue = ThreadLocalRandom.current().nextDouble(0, 10000);
            BigDecimal value = BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP);
            transaction.setAmount(value);
            transactionService.createTransaction(transaction);
        }
    }


    public void createTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setName("t-1");
        transaction1.setType("buy");
        transaction1.setAmount(new BigDecimal("135.7"));
        transactionService.createTransaction(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setName("t-2");
        transaction2.setType("sell");
        transaction2.setAmount(new BigDecimal("246.8"));
        transactionService.createTransaction(transaction2);
    }

}
