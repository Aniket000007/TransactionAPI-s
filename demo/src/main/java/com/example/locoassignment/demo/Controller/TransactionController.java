package com.example.locoassignment.demo.Controller;

import com.example.locoassignment.demo.Entity.SumRetriever;
import com.example.locoassignment.demo.Entity.Transactions;
import com.example.locoassignment.demo.Entity.TransactionsRetriever;
import com.example.locoassignment.demo.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactionservice")
public class TransactionController {

    @Autowired
    private TransactionService service;

    //doesnt take -ve transaction id
    //doesnt take -ve amount
    //doesnt take null for amount and type in transaction
    @PutMapping("/transaction/{transaction_id}")
    public ResponseEntity<?> putTransaction(@PathVariable("transaction_id") Long transaction_id, @RequestBody Transactions transactions){
        if(transaction_id<0){
            return new ResponseEntity<>("Negative transaction id passed ",HttpStatus.BAD_REQUEST);
        }
        if(transactions.getAmount()==null||transactions.getAmount()<0){
            return new ResponseEntity<>("Invalid amount entered  ",HttpStatus.BAD_REQUEST);
        }
        if(transactions.getType()==null){
            return new ResponseEntity<>("Invalid type entered  ",HttpStatus.BAD_REQUEST);
        }
        try{
            service.saveTransaction(transaction_id,transactions);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //doesnt take -ve transaction id
    @GetMapping("/transaction/{transaction_id}")
    public ResponseEntity<TransactionsRetriever> getTransaction(
            @PathVariable("transaction_id") Long transactionId) {
        if(transactionId<0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TransactionsRetriever transaction = service.getTransaction(transactionId);
        if (transaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    //If list is empty no point returning empty list better is to say not found
    @GetMapping("/types/{type}")
    public ResponseEntity<?> getTransactionsByType(
            @PathVariable("type") String type) {
        List<Long> transactionIds = service.getTransactionIdsByType(type);
        if(transactionIds.isEmpty()){
            return new ResponseEntity<>("No records matching " + type + " found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transactionIds, HttpStatus.OK);
    }

    //doesnt accept negative transaction_id
    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<SumRetriever> getSumOfTransactions(
            @PathVariable("transaction_id") Long transactionId) {
        if(transactionId<0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        SumRetriever sum = service.calculateSum(transactionId);
        return new ResponseEntity<>(sum, HttpStatus.OK);
    }
}
