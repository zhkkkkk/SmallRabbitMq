package com.linzi.myrabbit.nativeapi.advance.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @Auther: zhuhuakun
 * @Date: 2018/10/31 23:08
 * @Description:
 */
public class MyConsumer extends DefaultConsumer {

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public MyConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("=========My Consumer listen=========");
        System.out.println("body===>" + new String(body));

        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Channel channel = super.getChannel();
        if (Integer.valueOf(String.valueOf(properties.getHeaders().get("num"))) == 0) {
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        } else {
            channel.basicAck(envelope.getDeliveryTag(), false);
        }
    }
}
