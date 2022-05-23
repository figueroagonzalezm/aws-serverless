package com.aws.apis.ordersapi.dto;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int id;
    private String itemName;
    private int quantity;

    public static Order map(Map<String, AttributeValue> item){
        return new Order(Integer.parseInt(item.get("id").getN()),
                item.get("itemName").getS(),
                Integer.parseInt(item.get("quantity").getN()));
    }
}
