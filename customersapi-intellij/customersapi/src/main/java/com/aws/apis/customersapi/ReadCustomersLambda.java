package com.aws.apis.customersapi;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.apis.customersapi.dto.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ReadCustomersLambda {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    public APIGatewayProxyResponseEvent readCustomers(APIGatewayProxyRequestEvent request, Context context)
            throws JsonProcessingException {

        ScanResult scanResult = dynamoDB.scan(new ScanRequest().withTableName(System.getenv("CUSTOMERS_TABLE")));
        List<Customer> customers = scanResult.getItems().stream()
                .map(Customer::map)
                .collect(Collectors.toList());

        String jsonOutput = objectMapper.writeValueAsString(customers);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(jsonOutput);
    }
}
