package com.linzi.myrabbit.nativeapi;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/30 16:15
 * @Description:
 */
public class TopicProductDemo1 {
    public static void main(String[] args) throws Exception {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.137.88");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "topic_native_exchange_test";
        String routingKey1 = "topic.native.route";
        String routingKey2 = "topic.native.route.suffix";

        String msg = "RabbitMQ_direct exchange";
        // 发送消息
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName, routingKey1, null, msg.getBytes());
            channel.basicPublish(exchangeName, routingKey2, null, msg.getBytes());
        }

        channel.close();
        connection.close();
    }
}
