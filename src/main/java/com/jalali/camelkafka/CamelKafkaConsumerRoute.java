package com.jalali.camelkafka;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelKafkaConsumerRoute extends  RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("kafka:my-topic?brokers=localhost:9092&groupId=my-group")
                .aggregate(constant(true), (oldExchange, newExchange) -> {
                    // Aggregate the numbers received
                    int newNumber = newExchange.getIn().getBody(Integer.class);
                    int total = oldExchange == null ? newNumber : oldExchange.getIn().getBody(Integer.class) + newNumber;
                    newExchange.getIn().setBody(total);
                    return newExchange;
                })
                .completionInterval(60000) // Aggregate every 60 seconds
                .to("direct:report");

        from("direct:report")
                .process(exchange -> {
                    // Report the total numbers
                    int total = exchange.getIn().getBody(Integer.class);
                    System.out.println("Total numbers received: " + total);
                });
    }
}
