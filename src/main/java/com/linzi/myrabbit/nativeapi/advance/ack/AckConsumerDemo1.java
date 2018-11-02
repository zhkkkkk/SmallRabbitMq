package com.linzi.myrabbit.nativeapi.advance.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/31 16:59
 * @Description:QOS模式消费者 限流
 */
public class AckConsumerDemo1 {
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
        String routingKey = "ack.*";
        String queueName = "ack.native.queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        channel.basicConsume(queueName, false, new MyConsumer(channel));
    }
}
