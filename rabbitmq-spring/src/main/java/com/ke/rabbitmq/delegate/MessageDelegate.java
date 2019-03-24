package com.ke.rabbitmq.delegate;


import com.ke.rabbitmq.bo.Order;
import com.ke.rabbitmq.bo.User;

import java.io.File;
import java.util.Map;

/**
 * 自定义消息接收器
 */
public class MessageDelegate {

    // 默认规定的方法名，可改，参考MessageListenerAdapter.ORIGINAL_DEFAULT_LISTENER_METHOD
    public void handleMessage(byte[] messageBody) {
        System.err.println("默认消息接收方法: " + new String(messageBody));
    }

    public void customMessageHandle(byte[] messageBody) {
        System.err.println("自定义消息接收方法: " + new String(messageBody));
    }

    public void customMessageConverterMessageHandle(String messageBody) {
        System.err.println("自定义消息转换器接收到消息: " + messageBody);
    }

    public void directQueueMessageHandler(byte[] messageBody) {
        System.err.println("directQueueMessageHandler 消息接收方法: " + new String(messageBody));
    }

    public void topicQueueMessageHandler(byte[] messageBody) {
        System.err.println("topicQueueMessageHandler 消息接收方法: " + new String(messageBody));
    }

    public void fanoutQueueMessageHandler(byte[] messageBody) {
        System.err.println("fanoutQueueMessageHandler 消息接收方法: " + new String(messageBody));
    }

    /**
     * json 消息
     * @param messgeBody
     */
    public void jsonMessageHandler(Map messgeBody) {
        System.err.println("jsonMessageHandler 消息接收方法: " + messgeBody);
    }

    public void methodHandler(User user) {
        System.err.println("methodHandler#user 消息接收方法: " + user.toString());
    }

    public void methodHandler(Order order) {
        System.err.println("methodHandler#order 消息接收方法: " + order.toString());
    }

    public void methodHandler(File file) {
        System.err.println("methodHandler#file 消息接收方法: " + file.getPath());
    }
}

