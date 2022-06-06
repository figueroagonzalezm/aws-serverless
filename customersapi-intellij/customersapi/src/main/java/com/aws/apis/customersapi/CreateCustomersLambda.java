package com.aws.apis.customersapi;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.apis.customersapi.dto.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateCustomersLambda {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());

    public APIGatewayProxyResponseEvent createCustomer(APIGatewayProxyRequestEvent request, Context context) throws JsonProcessingException {
        LambdaLogger logger = context.getLogger();
        logger.log("APIGatewayProxyRequestEvent: " + request);

        Customer customer = objectMapper.readValue(request.getBody(), Customer.class);
        logger.log(customer.toString());
        Table customerTable = dynamoDB.getTable(System.getenv("CUSTOMERS_TABLE"));
        Item item = new Item().withPrimaryKey("id", customer.getId())
                .withString("firstName", customer.getFirstName())
                .withString("lastName", customer.getLastName())
                .withInt("rewardPoints", customer.getRewardPoints());
        PutItemOutcome putItemOutcome = customerTable.putItem(item);

        logger.log(":::::::::::::"+putItemOutcome.toString());
        return new APIGatewayProxyResponseEvent()
                .withBody(objectMapper.writeValueAsString(customer))
                .withStatusCode(200);
    }
}
