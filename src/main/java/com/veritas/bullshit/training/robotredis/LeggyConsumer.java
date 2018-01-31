package com.veritas.bullshit.training.robotredis;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class LeggyConsumer extends DefaultConsumer {

    private int name;
    private int charge;
    private String updated;

    public LeggyConsumer(Channel channel, int name, int charge) {
        super(channel);
        this.name = name;
        this.charge = charge;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(String.format("[%s] -> %s", name, message));
        updated = message + "-" + charge;
    }

    public String getUpdated() {
        return updated;
    }

}
