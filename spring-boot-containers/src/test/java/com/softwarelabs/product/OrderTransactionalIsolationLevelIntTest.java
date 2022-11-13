/*
package com.softwarelabs.product;

import com.softwarelabs.config.BaseIntegrationTest;
import com.softwarelabs.config.TransactionHelper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
*/
/*
 https://www.postgresql.org/docs/current/transaction-iso.html#:~:text=Read%20Committed%20is%20the%20default,query%20execution%20by%20concurrent%20transactions.
*//*

public class OrderTransactionalIsolationLevelIntTest extends BaseIntegrationTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TransactionHelper transactionHelper;


    @Test
    void testReadCommittedIsolationLevel_withSingleTransaction() {
        String productName = saveProduct();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(runnableThread(productName));
        gracefullyShutdown(executorService);
    }

    @Test
    void testReadCommittedIsolationLevel_withMultipleThreads() {
        UUID cardId = saveProduct();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(delayedRunnableThread(cardId));
        executorService.execute(runnableThread(cardId));
        gracefullyShutdown(executorService);
    }

    private void gracefullyShutdown(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    @NotNull
    private String saveProduct() {
        String productName = "product001";
        PersistantProduct product = new PersistantProduct(productName);
        return productName;
    }

    @NotNull
    private Runnable runnableThread(String productName) {
        return () -> transactionHelper.withTransaction(() -> {
            delay(100l);
            log.info("Runnable thread is starting");
            var productAfterInsert = productRepository.findByName(productName).orElseThrow();
            log.info("Runnable thread - productAfterInsert productName= {}", productAfterInsert.name());

            var newProduct = new PersistantProduct("Erhun Baycelik");
            productRepository.update(productAfterInsert);
            log.info("Runnable thread is updated");
            var cardAfterUpdate = cardRepository.findById(cardId).orElseThrow();
            log.info("Runnable thread - cardAfterUpdate nameOnCard= {}", cardAfterUpdate.getNameOnCard());
            log.info("Runnable thread is finished");
        });
    }

    @NotNull
    private Runnable delayedRunnableThread(UUID cardId) {
        return () -> transactionHelper.withTransaction(() -> {
            log.info("Delayed runnable thread is starting");

            var cardAfterInsert = cardRepository.findById(cardId).orElseThrow();
            log.info("Delayed runnable thread - cardAfterInsert nameOnCard= {}", cardAfterInsert.getNameOnCard());

            cardAfterInsert.setNameOnCard("Erhun Baycelik Delayed");
            cardRepository.update(cardAfterInsert);
            log.info("Delayed runnable thread is updated");
            var cardAfterUpdate = cardRepository.findById(cardId).orElseThrow();
            log.info("Delayed runnable thread - cardAfterUpdate nameOnCard= {}", cardAfterUpdate.getNameOnCard());
            delay(500l);
            log.info("Delayed runnable thread is finished");
        });
    }

    private void delay(long delayInMilliseconds) {
        try {
            Thread.sleep(delayInMilliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
*/
