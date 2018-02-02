package com.veritas.bullshit.training.rabbittutorials.r0;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;

import java.io.IOException;

public class Handler extends DefaultConsumer {

    private RScoredSortedSet<String> set;
    private RLock lock;

    public Handler(Channel channel, RScoredSortedSet<String> set, RLock lock) {
        super(channel);
        this.set = set;
        this.lock = lock;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        lock.lock();
        String message = new String(body, "UTF-8");
        System.out.printf(" [<] Received '%s'%n", message);
        String[] parts = message.split("-");
        set.add(Integer.parseInt(parts[1]), message);
        System.out.printf(" [V] Recorded message '%s' to redis set '%s'%n", message, set.getName());
        lock.unlock();
    }

}
