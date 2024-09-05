package com.example.locoassignment.demo.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
//class i have used to retrieve sum for the getsum function
//in future logic changes we can directly use it here instead of doing it through transactions class
public class SumRetriever {
    private double sum;
}
