package com.linzi.myrabbit.nativeapi.advance.returnmode;

import com.rabbitmq.client.*;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/31 16:59
 * @Description:return模式生产者
 */
public class ReturnConsumerDemo1 {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("192.168.137.88");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "return.native.exchange";
        String routingKey = "return.*";
        String queueName = "return.native.queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);


        // 消费者与队列监听关系绑定!!!!
        channel.basicConsume(queueName, true, new MyConsumer(channel));

    }
}
