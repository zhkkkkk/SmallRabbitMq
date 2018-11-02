package com.linzi.myrabbit.nativeapi.advance.confirm;

import com.rabbitmq.client.*;
import org.springframework.amqp.rabbit.support.Delivery;

import java.io.IOException;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/31 16:59
 * @Description:confirm模式生产者
 */
public class ConfirmConsumerDemo1 {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("192.168.137.88");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "confirm.native.exchange";
        String routingKey = "confirm.*";
        String queueName = "confirm.native.queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        // 消费者与队列监听关系绑定!!!!
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费者接受到消息 === > " + msg);
        }

    }
}
