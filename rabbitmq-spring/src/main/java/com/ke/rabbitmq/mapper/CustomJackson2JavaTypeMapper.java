package com.ke.rabbitmq.mapper;

import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;

/**
 * java 转换映射
 */
public class CustomJackson2JavaTypeMapper extends DefaultJackson2JavaTypeMapper {

    /**
     * 构造函数初始化信任所有pakcage
     */
    public CustomJackson2JavaTypeMapper() {
        super();
        setTrustedPackages("*");
    }
}
