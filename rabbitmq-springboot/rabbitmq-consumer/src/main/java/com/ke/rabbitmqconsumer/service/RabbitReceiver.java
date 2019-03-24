package com.ke.rabbitmqconsumer.service;

import com.ke.rabbitmqcommon.entity.Order;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RabbitReceiver {


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "rabbitmq_queue", durable = "true"),
            exchange = @Exchange(value = "rabbitmq-exchange", durable = "true", type = "topic", ignoreDeclarationExceptions = "true"),
            key = "rabbitmq.*"
         )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception{
          log.error("consumer receive message : {}", message.getPayload());

          Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);

          // 手动ack
          channel.basicAck(deliveryTag, false);
    }


    /**
     * 自定义消息类型接收器
     * @param order
     * @param channel
     * @param header
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}", durable = "${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
                    durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                    type = "${spring.rabbitmq.listener.order.exchange.type}",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
            key = "${spring.rabbitmq.listener.order.rountingkey}"
    )
    )
    @RabbitHandler
    public void onOrderMessage(@Payload Order order, Channel channel, @Headers Map<String, Object> header) throws Exception{
        log.error("consumer receive order message : {}", order.toString());

        Long deliveryTag = (Long) header.get(AmqpHeaders.DELIVERY_TAG);

        // 手动ack
        channel.basicAck(deliveryTag, false);
    }


}
