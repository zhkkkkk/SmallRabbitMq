package com.linzi.myrabbit.nativeapi.advance.DLX;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/31 16:59
 * @Description:QOS模式消费者 限流
 */
public class DLXConsumerDemo1 {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("192.168.137.88");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "dlx.test.native.exchange";
        String routingKey = "dlx.#";
        String queueTestName = "dlx.test.native.queue";

        String dlxName = "dlx.native.exchange";

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", dlxName);

        // 普通队列声明
        channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
        channel.queueDeclare(queueTestName, true, false, false, arguments);
        channel.queueBind(queueTestName, exchangeName, routingKey);

        channel.basicConsume(queueTestName, false, new MyConsumer(channel));

        String dlxroutingKey = "#";
        String queueName = "dlx.native.queue";

        // 死信队列声明
        channel.exchangeDeclare(dlxName, "topic", true, false, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, dlxName, dlxroutingKey);
        // 消费者与队列监听关系绑定!!!!  做限流autoAck需要设置为false
//        channel.basicConsume(queueName, false, new MyConsumerDLX(channel));

    }
}
