package com.linzi.myrabbit.nativeapi.basics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.HashMap;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/30 14:09
 * @Description: main方法发送消息
 */
public class FanoutConsumeDemo1 {
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
        String queueName = "fanout_native_queue_test";
        String exchangeName = "fanout_native_exchange_test";
        String type = "fanout";

        channel.queueDeclare(queueName, true, false, false, new HashMap<>());
        channel.exchangeDeclare(exchangeName, type, true, false, false, null);
        channel.queueBind(queueName, exchangeName, "");

        // 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        // 消费者监听队列 自动确认消息
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            // 阻塞获取消息
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());

            System.out.println("fanout模式 消费者消费消息 ===> " + msg);
        }

    }
}
