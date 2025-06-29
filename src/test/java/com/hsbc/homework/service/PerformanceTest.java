package com.hsbc.homework.service;

import com.hsbc.homework.models.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class PerformanceTest {
    public static int TRANSACTION_COUNT = 10000;

    @Autowired
    private TransactionService transactionService;

    @Test
    public void testGetAllTransactions() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
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
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.printf("Use %s millisecond create %s transactions!%n", totalTime, TRANSACTION_COUNT);
    }
}
