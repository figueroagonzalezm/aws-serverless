package com.aws.apis.productsapi;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.apis.productsapi.entity.ProductEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateProductLambda {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

    public APIGatewayProxyResponseEvent createProduct(APIGatewayProxyRequestEvent request, Context context) throws JsonProcessingException {
        LambdaLogger logger = context.getLogger();
        logger.log("Starting product creation");
        ProductEntity productToSave = null;
        String message = "Product CREATED successfully";
        int statusCode = 200;
        try {
            productToSave = objectMapper.readValue(request.getBody(), ProductEntity.class);
            ProductEntity productEntity = dynamoDBMapper.load(ProductEntity.class, productToSave.getId());
            if (productEntity != null) {
                logger.log("Product already exists, proceed to update");
                message = "Product UPDATED successfully";
            }
            dynamoDBMapper.save(productToSave);
        } catch (JsonProcessingException e) {
            message = "Error while parsing request body";
            logger.log(message);
            statusCode = 400;
        }
        return new APIGatewayProxyResponseEvent().withBody("{\"response\": \"" + message + "\" }")
                .withStatusCode(statusCode);
    }
}
