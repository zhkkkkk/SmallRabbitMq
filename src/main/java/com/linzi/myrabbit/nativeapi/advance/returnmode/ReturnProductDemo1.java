package com.linzi.myrabbit.nativeapi.advance.returnmode;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/31 16:59
 * @Description:return模式生产者
 */
public class ReturnProductDemo1 {
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
        String routingKey = "return.test";
        String errorRoutingKey = "error.test";

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("========return listener handle========");
                System.out.println("replyCode===>" + replyCode);
                System.out.println("replyText===>" + replyText);
                System.out.println("exchange===>" + exchange);
                System.out.println("routingKey===>" + routingKey);
                System.out.println("properties===>" + properties);
                System.out.println("body===>" + new String(body));
            }
        });

        Map<String, Object> headers = new HashMap<>();
        headers.put("key", "value");
        AMQP.BasicProperties properties = new AMQP.BasicProperties();
        properties.builder()
                .contentEncoding("UTF-8")
                .headers(headers)
                .build();

        String msg = "Hello RabbitMQ return mode run!!!";
        // mandatory要设置为true 否则无效
        channel.basicPublish(exchangeName, routingKey, true, properties, msg.getBytes());
//        channel.basicPublish(exchangeName, errorRoutingKey, true, properties, msg.getBytes());

    }
}
