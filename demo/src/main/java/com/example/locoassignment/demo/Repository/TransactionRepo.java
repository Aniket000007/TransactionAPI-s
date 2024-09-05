package com.example.locoassignment.demo.Repository;

import com.example.locoassignment.demo.Entity.Transactions;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transactions,Long> {

    //selects transaction ids and stores in list for a given type
    @Query(value="SELECT t.transaction_id FROM loco t WHERE t.type = :type",nativeQuery = true)
    List<Long> findByType(@Param("type")String type);

    //selects rows where transaction_id matches given id
    @Query(value = "SELECT * FROM loco WHERE transaction_id = :transactionId",nativeQuery = true)
    Transactions findByyId(@Param("transactionId") Long transactionId);

    //used a recursive call to calculate sum of amount of children and grandchildren
    //used union all instead of union to maintain tree structure
    //inner join finds all transactions where the parent_id matches a transaction_id from the previous step
    //finally sum is returned
    @Query(value = "WITH RECURSIVE TransactionTree AS (" +
            "    SELECT transaction_id, amount, parent_id " +
            "    FROM loco " +
            "    WHERE transaction_id = :transactionId " +
            "    UNION ALL " +
            "    SELECT t.transaction_id, t.amount, t.parent_id " +
            "    FROM loco t " +
            "    INNER JOIN TransactionTree tt " +
            "    ON t.parent_id = tt.transaction_id " +
            ") " +
            "SELECT SUM(amount) AS total_sum " +
            "FROM TransactionTree",
            nativeQuery = true)
    double calculateSum(@Param("transactionId") Long transactionId);


    //inserts values into the table the given values
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO loco (transaction_id, amount, type, parent_id) VALUES (:transactionId, :amount, :type, :parentId)",
            nativeQuery = true)
    void insertTransaction(@Param("transactionId") Long transactionId,
                           @Param("amount") Double amount,
                           @Param("type") String type,
                           @Param("parentId") Long parentId);
}
