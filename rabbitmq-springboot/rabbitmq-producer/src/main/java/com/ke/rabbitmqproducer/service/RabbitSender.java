package com.ke.rabbitmqproducer.service;

import com.ke.rabbitmqcommon.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * 消息发送器
 */
@Slf4j
@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息路由成功
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
             log.error("correlationData: {}, ack: {}, cause: {}", correlationData, ack, cause);

             // 自定根据ack 做不同的业务处理
        }
    };


    /**
     * 消息路由失败
     */
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message,
                                    int replyCode, String replyText, String exchange, String routingKey) {
            log.error("message: {}, replyCode: {}, replyText: {}, exchange: {}, routingKey: {}",
                    message, replyCode, replyText, exchange, routingKey);
        }
    };


    public void send(Object message, Map<String, Object> properties) throws Exception {
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, messageHeaders);

        // 设置路由成功和失败的回调事件
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        CorrelationData correlationData = new CorrelationData();
        // 根据业务生成唯一ID， 一定要确保全局唯一， 以后可根据该ID 做相应业务处理
        String id = UUID.randomUUID().toString().substring(0, 9) + System.currentTimeMillis();
        correlationData.setId(id);

        rabbitTemplate.convertAndSend("rabbitmq-exchange", "rabbitmq.rountingkey", msg, correlationData);
    }


    /**
     * 发送自定义对象
     * @param order {@link Order}
     * @throws Exception
     */
    public void send(Order order) throws Exception {
        // 设置路由成功和失败的回调事件
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        CorrelationData correlationData = new CorrelationData();
        // 根据业务生成唯一ID， 一定要确保全局唯一， 以后可根据该ID 做相应业务处理
        String id = UUID.randomUUID().toString().substring(0, 9) + System.currentTimeMillis();
        correlationData.setId(id);

        rabbitTemplate.convertAndSend("rabbitmq-exchange", "rabbitmq.order.save", order, correlationData);
    }
}
