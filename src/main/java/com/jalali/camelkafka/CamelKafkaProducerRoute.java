package com.jalali.camelkafka;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;

@Component
public class CamelKafkaProducerRoute extends RouteBuilder {

    @Autowired
    private KafkaTemplate<String, Integer> kafkaTemplate;

    @Override
    public void configure() throws Exception {
        from("timer://myTimer?period=10000")
                .process(exchange -> {
                    // Generate a random number between 0 and 100
                    int randomNumber = (int) (Math.random() * 101);
                    // Send the random number to the Kafka topic
                    kafkaTemplate.send("my-topic", randomNumber);
                });

    }
}
