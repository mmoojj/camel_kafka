package com.jalali.camelkafka.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelKafkaProducerRoute extends RouteBuilder {



    @Override
    public void configure() throws Exception {
        from("timer://myTimer?period=10000")
                .process(exchange -> {
                    int randomNumber = (int) (Math.random() * 101);
                    exchange.getMessage().setBody(randomNumber);
                }).to("kafka:my-topic?brokers=localhost:9092");

    }
}
