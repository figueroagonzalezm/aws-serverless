package com.aws.apis.ordersapi;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.apis.ordersapi.dto.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateOrderLambda {

    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
    private final ObjectMapper objectMapper = new ObjectMapper();

    public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyRequestEvent request, Context context) throws JsonProcessingException {
        LambdaLogger logger = context.getLogger();
        logger.log("APIGatewayProxyRequestEvent::::: " + request.toString());
        Order order = objectMapper.readValue(request.getBody(), Order.class);

        Table table = dynamoDB.getTable(System.getenv("ORDERS_TABLE"));
        Item item = new Item()
                .withPrimaryKey("id", order.getId())
                .withString("itemName", order.getItemName())
                .withInt("quantity", order.getQuantity());
        PutItemOutcome putItemOutcome = table.putItem(item);
        logger.log("PutItemOutcome:::: " + putItemOutcome);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("Order ID: " + order.getId());
    }
}
