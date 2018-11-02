package com.linzi.myrabbit.nativeapi.advance.qos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/31 16:59
 * @Description:QOS模式消费者 限流
 */
public class QosConsumerDemo1 {
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
        String routingKey = "qos.*";
        String queueName = "qos.native.queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 参1: 消息的大小设置 0不限 参2:批量消费的消息数量 参3:作用域是channel还是当前consumer
        channel.basicQos(0, 1, false);

        // 消费者与队列监听关系绑定!!!!  做限流autoAck需要设置为false
        channel.basicConsume(queueName, false, new MyConsumer(channel));

    }
}
