package com.linzi.myrabbit.nativeapi.advance.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/31 16:59
 * @Description:QOS模式生产者
 */
public class AckProductDemo1 {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("192.168.137.88");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "ack.native.exchange";
        String routingKey = "ack.test";

        String msg = "Hello RabbitMQ ACK NACK run!!!";



        for (int i = 0;i < 5; i++) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("num", i);
            AMQP.BasicProperties properties = new AMQP.BasicProperties()
                    .builder()
                    .headers(headers)
                    .build();
            channel.basicPublish(exchangeName, routingKey, properties, msg.concat(String.valueOf(i)).getBytes());
        }

    }
}
