package com.linzi.myrabbit.nativeapi.advance.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/31 16:59
 * @Description: confirm模式消费者
 */
public class ConfirmProductDemo1 {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("192.168.137.88");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // 指定消息投递模式:confirm模式!!!  否则confirm监听器不生效
        channel.confirmSelect();

        String exchangeName = "confirm.native.exchange";
        String routingKey = "confirm.route";
        String msg = "Hello RabbitMQ confirm message!!!";

        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("-----------ACK!!!----------");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("-----------NO ACK!!!----------");
            }
        });

//        channel.close();
//        connection.close();
    }
}
