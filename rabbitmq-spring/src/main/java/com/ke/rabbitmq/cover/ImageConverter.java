package com.ke.rabbitmq.cover;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

/**
 * 图片消息转换器
 */
public class ImageConverter implements MessageConverter {


    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        throw  new MessageConversionException("converter error!");
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.err.println("-----------------  Image converter ------------------");

        Object _extName = message.getMessageProperties().getHeaders().get("extName");
        String extName = _extName == null ? "png" : _extName.toString();

        byte[] messageBody = message.getBody();
        String fileName = UUID.randomUUID().toString();
        String filePath = "D:/upload/image/" + fileName + "." + extName;

        File file = new File(filePath);
        try {
            Files.copy(new ByteArrayInputStream(messageBody), file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
