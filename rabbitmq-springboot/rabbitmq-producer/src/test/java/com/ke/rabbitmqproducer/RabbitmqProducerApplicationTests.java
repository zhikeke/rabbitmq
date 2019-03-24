package com.ke.rabbitmqproducer;

import com.ke.rabbitmqcommon.entity.Order;
import com.ke.rabbitmqproducer.service.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqProducerApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitSender rabbitSender;

    @Test
    public void send() throws Exception{
        Map<String, Object> properties = new HashMap<>();
        properties.put("ext", "keke");

        rabbitSender.send("Hello RabbitMQ!", properties);
    }

    @Test
    public void sendOrder() throws Exception{
        Order order = new Order("001", "订单一");

        rabbitSender.send(order);
    }
}
