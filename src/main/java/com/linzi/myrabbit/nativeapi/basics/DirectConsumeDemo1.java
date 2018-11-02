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
public class DirectConsumeDemo1 {
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

        String exchangeName = "direct_native_exchange_test";
        String exchangeType = "direct";
        String routingKey = "direct.native.route";
        String queueName = "direct_native_queue_test";

        // 声明交换机/队列/绑定关系
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 定义消费者 并绑定队列
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("原生api==direct模式接收消息 ===> " + msg);
        }
    }
}
