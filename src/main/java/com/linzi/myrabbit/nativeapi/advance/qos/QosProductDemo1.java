package com.linzi.myrabbit.nativeapi.advance.qos;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/31 16:59
 * @Description:QOS模式生产者
 */
public class QosProductDemo1 {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("192.168.137.88");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "qos.native.exchange";
        String routingKey = "qos.test";

        String msg = "Hello RabbitMQ QOS run!!!";

        for (int i = 0;i < 5; i++) {
            channel.basicPublish(exchangeName, routingKey, null, msg.concat(String.valueOf(i)).getBytes());
        }

    }
}
