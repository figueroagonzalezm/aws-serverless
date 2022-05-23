package com.aws.apis.ordersapi;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.apis.ordersapi.dto.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ReadOrdersLambda {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    public APIGatewayProxyResponseEvent getOrders(APIGatewayProxyRequestEvent request, Context context) throws JsonProcessingException {
        LambdaLogger logger = context.getLogger();
        Order order = new Order(123, "Mac Book Pro", 100);

        ScanResult scanResult = dynamoDB.scan(new ScanRequest().withTableName(System.getenv("ORDERS_TABLE")));
        List<Order> orders = scanResult.getItems().stream()
                .map(Order::map)
                .collect(Collectors.toList());

        String jsonOutput = objectMapper.writeValueAsString(orders);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(jsonOutput);
    }
}
