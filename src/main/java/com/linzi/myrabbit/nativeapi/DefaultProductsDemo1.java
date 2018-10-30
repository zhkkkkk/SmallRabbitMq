package com.linzi.myrabbit.nativeapi;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/30 14:09
 * @Description: main方法发送消息
 */
public class DefaultProductsDemo1 {
    public static void main(String[] args) throws Exception {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.137.88");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        // 获取连接
        Connection connection = connectionFactory.newConnection();
        // 创建channel通道
        Channel channel = connection.createChannel();

        Map<String, Object> headers = new HashMap<>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000")
                .headers(headers)
                .build();

        // exchange为空的话 发送消息到default exchange中, 该交换机将消息路由到routingKey与消息队列名称一致的队列中
        for (int i = 0; i < 5; i++) {
            channel.basicPublish("", "test001", properties, "Hello nativeapi RabbbitMq!".getBytes());
        }

        channel.close();
        connection.close();
    }
}
