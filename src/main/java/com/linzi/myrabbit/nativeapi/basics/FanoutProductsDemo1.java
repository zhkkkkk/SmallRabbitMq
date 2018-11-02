package com.linzi.myrabbit.nativeapi.basics;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/30 14:09
 * @Description: main方法发送消息
 */
public class FanoutProductsDemo1 {
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

        String exchangeName = "fanout_native_exchange_test";

        // exchange为空的话 发送消息到default exchange中, 该交换机将消息路由到routingKey与消息队列名称一致的队列中
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName, "", null, "Hello fanout nativeapi RabbbitMq!".getBytes());
        }

        channel.close();
        connection.close();
    }
}
