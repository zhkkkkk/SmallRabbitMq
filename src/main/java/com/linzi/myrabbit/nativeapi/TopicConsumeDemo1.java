package com.linzi.myrabbit.nativeapi;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/30 16:15
 * @Description:
 */
public class TopicConsumeDemo1 {
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
        String routingKey1 = "topic.native.*";
        String queueName1 = "topic_native_queue_test1";

        // 声明交换机/队列/绑定关系
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);

        channel.queueDeclare(queueName1, true, false, false, null);

        channel.queueBind(queueName1, exchangeName, routingKey1);

        // 定义消费者 并绑定队列
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName1, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("原生api==topic模式 Consumer1 接收消息 ===> " + msg);
        }
    }
}
