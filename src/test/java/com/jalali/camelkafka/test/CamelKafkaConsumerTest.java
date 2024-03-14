package com.jalali.camelkafka.test;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CamelKafkaConsumerTest {



    @Autowired
    private ProducerTemplate producerTemplate;

    @Test
    public void testCamelKafkaConsumerRoute() throws Exception {
        // Prepare message
        int randomNumber = 42;
        producerTemplate.sendBody("kafka:my-topic?brokers=localhost:9092&groupId=my-group", randomNumber);

        // Optionally add assertions here
    }

    // This configures the Camel route to be used in the test
    @SpringBootTest
    public static class TestConfig extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("kafka:my-topic?brokers=localhost:9092&groupId=my-group")
                    .aggregate(constant(true), (oldExchange, newExchange) -> {
                        int newNumber = newExchange.getIn().getBody(Integer.class);
                        int total = oldExchange == null ? newNumber : oldExchange.getIn().getBody(Integer.class) + newNumber;
                        newExchange.getIn().setBody(total);
                        return newExchange;
                    })
                    .completionInterval(60000)
                    .to("direct:report");

            from("direct:report")
                    .process(exchange -> {
                        int total = exchange.getIn().getBody(Integer.class);
                        System.out.println("Total numbers received: " + total);
                    });
        }
    }
}
