package com.veritas.bullshit.training.rabbittutorials.r2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import org.redisson.api.RedissonClient;

import java.io.IOException;

public class Worker extends Thread {

    private Channel channel;
    private String queueName;
    private int workerNumber;
    private RedissonClient client;

    Worker(Channel channel, RedissonClient client, String queueName, int workerNumber) {
        this.channel = channel;
        this.client = client;
        this.queueName = queueName;
        this.workerNumber = workerNumber;
    }

    @Override
    public void run() {
        try {
            boolean durable = true;
            channel.queueDeclare(queueName, durable, false, false, null);
            System.out.printf("[*] Started worker #%s%n", workerNumber);
            Consumer consumer = new Handler2(channel, client, workerNumber);
            boolean autoAck = false;
            channel.basicConsume(queueName, autoAck, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
