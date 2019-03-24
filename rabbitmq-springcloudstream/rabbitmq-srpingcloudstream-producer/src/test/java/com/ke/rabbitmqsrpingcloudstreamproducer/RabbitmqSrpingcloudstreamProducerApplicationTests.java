package com.ke.rabbitmqsrpingcloudstreamproducer;

import com.ke.rabbitmqsrpingcloudstreamproducer.service.RabbitmqSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSrpingcloudstreamProducerApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitmqSender rabbitmqSender;

    @Test
    public void send() {

        for (int i = 0; i < 10; i++) {
            rabbitmqSender.sendMessage("Hello RabbitMQ! " + i, new HashMap<>());
        }
    }

}
