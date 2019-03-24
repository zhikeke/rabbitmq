package com.ke.rabbitmqsrpingcloudstreamproducer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@EnableBinding(Barista.class)
@Service
public class RabbitmqSender {

    @Autowired
    private Barista barista;

    public String sendMessage(Object message, Map<String, Object> properties) {
        try {
            MessageHeaders messageHeaders = new MessageHeaders(properties);
            Message msg = MessageBuilder.createMessage(message, messageHeaders);

            boolean sendStatus = barista.logoutput().send(msg);

            log.error("-----------------  sending  ---------------");
            log.error("发送数据: {}, sendStatus: {}", message, sendStatus);
        } catch (Exception e) {
            log.error("----------  error ------------");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

}
