package com.veritas.bullshit.training.dust;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;

import java.io.IOException;

public class CConsumer extends DefaultConsumer {

    private String queueName;
    private RScoredSortedSet<String> set;
    private RLock lock;

    CConsumer(Channel channel, String queueName, RScoredSortedSet set, RLock lock) {
        super(channel);
        this.queueName = queueName;
        this.set = set;
        this.lock = lock;
    }

    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        lock.lock();
        String message = new String(body, "UTF-8");
        System.out.print(String.format("[<] Received report from %s: %s\n", queueName, message));
        String[] parts = message.split(" --> ");
        set.add(Integer.parseInt(parts[1]), message);
        System.out.print(String.format("[V] Recorded the message '%s' to %s\n", message, set.getName()));
        lock.unlock();
    }

}
