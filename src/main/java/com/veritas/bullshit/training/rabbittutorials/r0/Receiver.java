package com.veritas.bullshit.training.rabbittutorials.r0;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;

import java.io.IOException;

public class Receiver extends Thread{

    private String queueuName;
    private String setName;
    private Channel channel;
    private RedissonClient client;

    public Receiver(String queueName, String setName, Channel channel, RedissonClient client) {
        this.queueuName = queueName;
        this.setName = setName;
        this.channel = channel;
        this.client = client;
    }

    public void run() {
        try {
            channel.queueDeclare(queueuName, false, false, false, null);
            System.out.println(" [*] Receiver was initialized. To exit press CTRL+C");

            RScoredSortedSet<String> set = client.getScoredSortedSet(setName);
            RLock lock = client.getReadWriteLock(setName + "lock").writeLock();
            Consumer consumer = new Handler(channel, set, lock);
            channel.basicConsume(queueuName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
