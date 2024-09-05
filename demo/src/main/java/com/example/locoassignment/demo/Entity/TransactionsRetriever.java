package com.example.locoassignment.demo.Entity;
import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter

//Class which i have used to return all values instead of transaction_id
//in future if return things change we can directly change here instead of the main transactions class
public class TransactionsRetriever {
    private Double amount;
    private String type;
    private Long parent_id;
}
