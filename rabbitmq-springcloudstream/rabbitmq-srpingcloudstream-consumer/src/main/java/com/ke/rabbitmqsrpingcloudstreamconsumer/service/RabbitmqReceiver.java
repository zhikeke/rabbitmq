package com.ke.rabbitmqsrpingcloudstreamconsumer.service;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@EnableBinding(Barista.class)
@Service
public class RabbitmqReceiver {

    @StreamListener(Barista.INPUT_CHANNEL)
    public void receiver(Message message) throws Exception{
        Channel channel = (Channel) message.getHeaders().get(AmqpHeaders.CHANNEL);

        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);

        log.error("receiver message: {}", message);

        // 手动ack
        channel.basicAck(deliveryTag, false);
    }
}
