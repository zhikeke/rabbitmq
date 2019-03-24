package com.ke.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ke.rabbitmq.bo.Order;
import com.ke.rabbitmq.bo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RabbitmqSpringApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testRabbitAdmin() {
        // 定义exchange
        rabbitAdmin.declareExchange(new DirectExchange("test.direct.exchange", false, false, new HashMap<>()));
        rabbitAdmin.declareExchange(new TopicExchange("test.topic.exchange", false, false, new HashMap<>()));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout.exchange", false, false, new HashMap<>()));

        // 定义queue
        rabbitAdmin.declareQueue(new Queue("test.queue", false, false, false, new HashMap<>()));

        // queue 绑定exchange
        rabbitAdmin.declareBinding(
                new Binding("test.queue", Binding.DestinationType.QUEUE,
                        "test.direct.exchange", "user.#", new HashMap<>()));

    }

    @Test
    public void testSendMessage() {
        // 创建消息内容
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "消息描述...");
        messageProperties.getHeaders().put("type", "自定义消息类型...");

        Message message = new Message("Hello RabbitMQ!".getBytes(), messageProperties);

        rabbitTemplate.convertAndSend("directExchange", "user.#", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.err.println("--------  方法发送前添加额外属性  ----------");
                message.getMessageProperties().getHeaders().put("attr", "额外属性...");

                return message;
            }
        });

        rabbitTemplate.convertAndSend("topicExchange", "user.#", "RabbitMQ topicExchange".getBytes());
        rabbitTemplate.convertAndSend("fanoutExchange","user.#", "RabbitMQ fanoutExchange".getBytes());

    }

    /**
     * 测试json转换
     * @throws Exception
     */
    @Test
    public void sendJsonMessage() throws Exception{
        User user = new User("科科", "male");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        MessageProperties messageProperties = new MessageProperties();
        // 注意一定要把contentType 设置为application/json
        messageProperties.setContentType("application/json");

        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("topicExchange", "user.#", message);
    }

    @Test
    public void sendJavaObjectMessage() throws Exception{
        User user = new User("科科", "male");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        MessageProperties messageProperties = new MessageProperties();
        // 注意一定要把contentType 设置为application/json
        messageProperties.setContentType("application/json");
        // 注意handler, 注意路径是接收方的BO路径
        messageProperties.getHeaders().put("__TypeId__", "com.ke.rabbitmq.bo.User");

        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("topicExchange", "user.#", message);
    }


    @Test
    public void sendJavaObjectMessage2() throws Exception{
        User user = new User("科科", "male");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        MessageProperties messageProperties = new MessageProperties();
        // 注意一定要把contentType 设置为application/json
        messageProperties.setContentType("application/json");
        // 注意handler
        messageProperties.getHeaders().put("__TypeId__", "user");

        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("topicExchange", "user.#", message);


        Order order = new Order("111", "订单描述");

        String json2 = objectMapper.writeValueAsString(order);

        // 注意handler
        messageProperties.getHeaders().put("__TypeId__", "order");

        Message message2 = new Message(json2.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("topicExchange", "user.#", message2);
    }

    @Test
    public void testSendConventerMessage() throws Exception{
//        byte[] messageBody = Files.readAllBytes(Paths.get("D:/upload/image/ke.jpg"));
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("image/png");
//        messageProperties.getHeaders().put("extName", "png");
//
//        Message message = new Message(messageBody, messageProperties);
//        rabbitTemplate.convertAndSend("", "imageQueue", message);


        byte[] messageBody = Files.readAllBytes(Paths.get("D:/upload/image/ke.pdf"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/pdf");

        Message message = new Message(messageBody, messageProperties);
        rabbitTemplate.convertAndSend("", "pdfQueue", message);
    }
}
