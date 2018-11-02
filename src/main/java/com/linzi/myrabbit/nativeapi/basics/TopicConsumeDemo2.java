package com.linzi.myrabbit.nativeapi.basics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/30 16:15
 * @Description:
 */
public class TopicConsumeDemo2 {
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
        String exchangeType = "topic";
        String routingKey2 = "topic.native.#";
        String queueName2 = "topic_native_queue_test2";

        // 声明交换机/队列/绑定关系
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);

        channel.queueDeclare(queueName2, true, false, false, null);

        channel.queueBind(queueName2, exchangeName, routingKey2);

        // 定义消费者 并绑定队列
        QueueingConsumer queueingConsumer2 = new QueueingConsumer(channel);
        channel.basicConsume(queueName2, true, queueingConsumer2);

        while (true) {
            QueueingConsumer.Delivery delivery2 = queueingConsumer2.nextDelivery();
            String msg2 = new String(delivery2.getBody());
            System.out.println("原生api==topic模式 Consumer2 接收消息 ===> " + msg2);
        }
    }
}
