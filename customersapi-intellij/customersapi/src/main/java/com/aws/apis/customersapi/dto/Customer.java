package com.aws.apis.customersapi.dto;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private int rewardPoints;

    public static Customer map(Map<String, AttributeValue> item){
        return new Customer(Integer.parseInt(item.get("id").getN()),
                item.get("firstName").getS(),
                item.get("lastName").getS(),
                Integer.parseInt(item.get("rewardPoints").getN()));
    }
}
