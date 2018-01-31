package com.veritas.bullshit.training.rabbies;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.redisson.api.RScoredSortedSet;

import java.io.IOException;

public class Handler extends DefaultConsumer {

    private String queueName;
    private String setName;
    private RScoredSortedSet<String> set;

    Handler(Channel channel, String queueName, String setName, RScoredSortedSet<String> set) {
        super(channel);
        this.queueName = queueName;
        this.setName = setName;
        this.set = set;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.print(String.format("[<] Received report from %s: %s", queueName, message));
        String[] things = message.split(" --> ");
        //System.out.print(String.format("Still there (%s)\n", Integer.parseInt(things[1])));
        //set.add(1, message);
        System.out.print(String.format("[v] Message recorded to redis set '%s': %s", setName, message));
    }

}
