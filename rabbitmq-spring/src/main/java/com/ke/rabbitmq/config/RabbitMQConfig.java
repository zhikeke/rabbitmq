package com.ke.rabbitmq.config;

import com.ke.rabbitmq.cover.ImageConverter;
import com.ke.rabbitmq.cover.PDFConverter;
import com.ke.rabbitmq.cover.TextMessageConverter;
import com.ke.rabbitmq.delegate.MessageDelegate;
import com.ke.rabbitmq.mapper.CustomJackson2JavaTypeMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@ComponentScan(basePackages = "com.ke.rabbitmq.*")
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("192.168.120.100:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    // 通过Bean 的方式来建立exchange
    @Bean
    public Exchange directExchange() {
        return new DirectExchange("directExchange", false, false, new HashMap<>());
    }

    @Bean
    public Exchange topicExchange() {
        return new TopicExchange("topicExchange", false, false, new HashMap<>());
    }

    @Bean
    public Exchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange", false, false, new HashMap<>());
    }

    // 通过bean方式创建queue
    @Bean
    public Queue directQueue() {
        return new Queue("directQueue", false);
    }

    @Bean
    public Queue topicQueue() {
        return new Queue("topicQueue", false);
    }

    @Bean
    public Queue fanoutQueue() {
        return new Queue("fanoutQueue", false);
    }

    @Bean
    public Queue imageQueue() {
        return new Queue("imageQueue", false);
    }

    @Bean
    public Queue pdfQueue() {
        return new Queue("pdfQueue", false);
    }


    // 通过bean 方式来 binding
    @Bean
    public Binding directQueueBinding() {
        return new Binding("directQueue", Binding.DestinationType.QUEUE, "directExchange", "user.#", new HashMap<>());
    }

    @Bean
    public Binding topicQueueBinding() {
        return new Binding("topicQueue", Binding.DestinationType.QUEUE, "topicExchange", "user.#", new HashMap<>());
    }

    @Bean
    public Binding fanoutQueueBinding() {
        return new Binding("fanoutQueue", Binding.DestinationType.QUEUE, "fanoutExchange", "user.#", new HashMap<>());
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }


    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        // 设置监听队列
        messageListenerContainer.setQueues(directQueue(), topicQueue(), fanoutQueue(), imageQueue(), pdfQueue());
        messageListenerContainer.setConcurrentConsumers(1);
        messageListenerContainer.setMaxConcurrentConsumers(5);
        messageListenerContainer.setDefaultRequeueRejected(false);
        messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        messageListenerContainer.setExposeListenerChannel(true);
        messageListenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queueName) {
                return queueName + "_" + UUID.randomUUID().toString();
            }
        });

//        messageListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                System.err.println("------- 接收到消息: " + new String(message.getBody()) + " -------------");
//            }
//        });

        // 自定义消息监听器
        messageListenerContainer.setMessageListener(messageListenerAdapter());

        return messageListenerContainer;
    }


    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());

        // 自定义消息接收方法名
        /*
            messageListenerAdapter.setDefaultListenerMethod("customMessageHandle");
        */


        // 自定义消息转换器, 如果转换结果不为byte数组，记得自定义消息监听器监听消息
        /*
            messageListenerAdapter.setMessageConverter(new CustomMessageConverter());
            messageListenerAdapter.setDefaultListenerMethod("customMessageConverterMessageHandle");
         */


        //  实现不同消息队列对应不同的消息处理方法
        /*
            Map<String, String> queueOrTagToMethodName = new HashMap<>();
            queueOrTagToMethodName.put("directQueue", "directQueueMessageHandler");
            queueOrTagToMethodName.put("topicQueue", "topicQueueMessageHandler");
            queueOrTagToMethodName.put("fanoutQueue", "fanoutQueueMessageHandler");

            messageListenerAdapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
         */


        // 支持json格式的转换器
        /*
            messageListenerAdapter.setDefaultListenerMethod("jsonMessageHandler");
            messageListenerAdapter.setMessageConverter(new Jackson2JsonMessageConverter());
         */


        // 支持java对象格式转换
        /*
              messageListenerAdapter.setDefaultListenerMethod("methodHandler");

              Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
              // 注意把TRUSTED_PACKAGES 改为 "*"
              CustomJackson2JavaTypeMapper javaTypeMapper = new CustomJackson2JavaTypeMapper();
              jsonMessageConverter.setJavaTypeMapper(javaTypeMapper);

              messageListenerAdapter.setMessageConverter(jsonMessageConverter);
        */

        // 支持java 多对象映射表
        /*
            messageListenerAdapter.setDefaultListenerMethod("methodHandler");

            Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
            // 注意把TRUSTED_PACKAGES 改为 "*"
            CustomJackson2JavaTypeMapper javaTypeMapper = new CustomJackson2JavaTypeMapper();

            Map<String, Class<?>> idClassMapping = new HashMap<>();
            idClassMapping.put("user", com.ke.rabbitmq.bo.User.class);
            idClassMapping.put("order", com.ke.rabbitmq.bo.Order.class);

            javaTypeMapper.setIdClassMapping(idClassMapping);

            jsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
            messageListenerAdapter.setMessageConverter(jsonMessageConverter);
        */

        // 支持多个转换器
        messageListenerAdapter.setDefaultListenerMethod("methodHandler");

        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();

        TextMessageConverter textMessageConverter = new TextMessageConverter();
        converter.addDelegate("text", textMessageConverter);
        converter.addDelegate("html/text", textMessageConverter);
        converter.addDelegate("xml/text", textMessageConverter);
        converter.addDelegate("text/plain", textMessageConverter);

        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        converter.addDelegate("json", jsonMessageConverter);
        converter.addDelegate("application/json", jsonMessageConverter);


        ImageConverter imageConverter = new ImageConverter();
        converter.addDelegate("image/png", imageConverter);
        converter.addDelegate("image", imageConverter);

        PDFConverter pdfConverter = new PDFConverter();
        converter.addDelegate("application/pdf", pdfConverter);

        messageListenerAdapter.setMessageConverter(converter);

        return messageListenerAdapter;
    }

}
