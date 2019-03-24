package com.ke.rabbitmq.cover;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

public class TextMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(o.toString().getBytes(), messageProperties);
    }

    /**
     * 将其转换为字符串
     * @param message
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.err.println("-----------------  text messge conver ----------------");

        String contextType = (String) message.getMessageProperties().getHeaders().get("customContextType");

        if ("text".equals(contextType)) {
            return new String(message.getBody());
        }

        return message.getBody();
    }
}
