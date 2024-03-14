package com.jalali.camelkafka.test;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CamelKafkaProducerTest {


    @Autowired
    private ProducerTemplate producerTemplate;

    @Test
    public void testCamelKafkaProducerRoute() throws Exception {

        producerTemplate.sendBody("timer://myTimer?period=10000", null);

    }

    @SpringBootTest
    public static class TestConfig extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("direct:start")
                    .to("mock:result");
        }
    }
}