package com.example.locoassignment.demo.Service;


import com.example.locoassignment.demo.Entity.SumRetriever;
import com.example.locoassignment.demo.Entity.Transactions;
import com.example.locoassignment.demo.Entity.TransactionsRetriever;
import com.example.locoassignment.demo.Repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {


    @Autowired
    private TransactionRepo repo;

    public void saveTransaction(Long transactionId, Transactions transaction) {

        repo.insertTransaction(transactionId,
                transaction.getAmount(),
                transaction.getType(),
                transaction.getParent_id());

    }

    public TransactionsRetriever getTransaction(Long transactionId) {

        Transactions transactions = repo.findByyId(transactionId);
        if(transactions == null ){
            return null;
        }
        //If transaction_id doesn't exist in table no point creating object to retrieve the data related to that id
        return new TransactionsRetriever(transactions.getAmount(),
                                        transactions.getType(),
                                        transactions.getParent_id());

    }

    public List<Long> getTransactionIdsByType(String type) {

        return repo.findByType(type);

    }

    public SumRetriever calculateSum(Long transactionId) {

        double sum= repo.calculateSum(transactionId);
        SumRetriever sumOfTransactions = new SumRetriever(sum);
        return sumOfTransactions;

    }
}
