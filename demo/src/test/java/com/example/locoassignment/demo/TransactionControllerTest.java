package com.example.locoassignment.demo;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.locoassignment.demo.Controller.TransactionController;
import com.example.locoassignment.demo.Entity.SumRetriever;
import com.example.locoassignment.demo.Entity.Transactions;
import com.example.locoassignment.demo.Entity.TransactionsRetriever;
import com.example.locoassignment.demo.Service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class TransactionControllerTest {

    @Mock
    private TransactionService service;

    @InjectMocks
    private TransactionController controller;

    public TransactionControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPutTransaction_NegativeTransactionId() {
        Transactions transaction = new Transactions();
        transaction.setAmount(100.0);
        transaction.setType("TestType");

        ResponseEntity<?> response = controller.putTransaction(-1L, transaction);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Negative transaction id passed ", response.getBody());
    }

    @Test
    public void testPutTransaction_InvalidAmount_Null() {
        Transactions transaction = new Transactions();
        transaction.setType("TestType");

        ResponseEntity<?> response = controller.putTransaction(1L, transaction);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid amount entered  ", response.getBody());
    }

    @Test
    public void testPutTransaction_InvalidAmount_Negative() {
        Transactions transaction = new Transactions();
        transaction.setAmount(-100.0);
        transaction.setType("TestType");

        ResponseEntity<?> response = controller.putTransaction(1L, transaction);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid amount entered  ", response.getBody());
    }

    @Test
    public void testPutTransaction_InvalidType() {
        Transactions transaction = new Transactions();
        transaction.setAmount(100.0);

        ResponseEntity<?> response = controller.putTransaction(1L, transaction);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid type entered  ", response.getBody());
    }

    @Test
    public void testPutTransaction_Success() {
        Transactions transaction = new Transactions();
        transaction.setAmount(100.0);
        transaction.setType("TestType");

        doNothing().when(service).saveTransaction(1L, transaction);

        ResponseEntity<?> response = controller.putTransaction(1L, transaction);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(service, times(1)).saveTransaction(1L, transaction);
    }

    @Test
    public void testPutTransaction_ExceptionHandling() {
        Transactions transaction = new Transactions();
        transaction.setAmount(100.0);
        transaction.setType("TestType");

        doThrow(new RuntimeException("Internal error")).when(service).saveTransaction(1L, transaction);

        ResponseEntity<?> response = controller.putTransaction(1L, transaction);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", response.getBody());
    }

    @Test
    public void testGetTransaction_NegativeTransactionId() {
        ResponseEntity<TransactionsRetriever> response = controller.getTransaction(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetTransaction_NotFound() {
        when(service.getTransaction(1L)).thenReturn(null);

        ResponseEntity<TransactionsRetriever> response = controller.getTransaction(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetTransaction_Success() {
        TransactionsRetriever transaction = new TransactionsRetriever(100.0,"TestType",null);

        when(service.getTransaction(1L)).thenReturn(transaction);

        ResponseEntity<TransactionsRetriever> response = controller.getTransaction(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100.0, response.getBody().getAmount());
        assertEquals("TestType", response.getBody().getType());
    }

    @Test
    public void testGetTransactionsByType_NoRecordsFound() {
        when(service.getTransactionIdsByType("TestType")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = controller.getTransactionsByType("TestType");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No records matching TestType found", response.getBody());
    }

    @Test
    public void testGetTransactionsByType_RecordsFound() {
        List<Long> transactionIds = Arrays.asList(1L, 2L, 3L);
        when(service.getTransactionIdsByType("TestType")).thenReturn(transactionIds);

        ResponseEntity<?> response = controller.getTransactionsByType("TestType");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(transactionIds, response.getBody());
    }

    @Test
    public void testGetSumOfTransactions_NegativeTransactionId() {
        ResponseEntity<SumRetriever> response = controller.getSumOfTransactions(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetSumOfTransactions_ValidTransaction() {
        SumRetriever sumRetriever = new SumRetriever(1500.0);

        when(service.calculateSum(1L)).thenReturn(sumRetriever);

        ResponseEntity<SumRetriever> response = controller.getSumOfTransactions(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1500.0, response.getBody().getSum());
    }
}
