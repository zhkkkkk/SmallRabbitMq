package com.linzi.myrabbit.nativeapi;

import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/30 14:09
 * @Description: main方法发送消息
 */
public class DefaultConsumeDemo1 {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.137.88");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // 定义队列
        String queueName = "test001";
        channel.queueDeclare(queueName, true, false, false, new HashMap<>());

        // 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        // 消费者监听队列 自动确认消息
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            // 阻塞获取消息
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            AMQP.BasicProperties properties = delivery.getProperties();
            Map<String, Object> headers = properties.getHeaders();
            System.out.println(headers);
            System.out.println("消费者消费消息 ===> " + msg);
        }

    }
}
