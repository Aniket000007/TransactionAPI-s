package com.example.locoassignment.demo.Entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

//Main transactions class holds all the data which we have in the table
public class Transactions {

    @Id
    private Long transaction_id;
    private Double amount;
    private String type;
    private Long parent_id;

}
