package com.veritas.bullshit.training.bunnies;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

public class BunnyRecorder extends Thread {

    private int max;
    private CountDownLatch latch;

    private RedissonClient client;
    private RScoredSortedSet<String> set;

    private Connection connection;
    private Channel channel;
    private BunnyConsumer consumer;

    private final String QUEUE_NAME = "counterBuffer";
    private final String SET_NAME = "lines";
    private final String HOST = "localhost";
    private final String ADDRESS = "127.0.0.1:6379";
    private final String LOGIN = "msxfusr";
    private final String PASSWORD = "msxfpwd";

    BunnyRecorder(int max, CountDownLatch latch) {
        this.max = max;
        this.latch = latch;

        Config config = new Config();
        config.useSingleServer()
                .setDatabase(0)
                .setAddress(ADDRESS)
                .setPassword(PASSWORD);
        this.client = Redisson.create(config);
        this.set = client.getScoredSortedSet(SET_NAME);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(LOGIN);
        factory.setPassword(PASSWORD);
        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            this.consumer = new BunnyConsumer(channel);
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    private void record(int i) {
        try {
            channel.basicConsume(QUEUE_NAME, true, consumer);
            System.out.print("CONSUMED: " + consumer.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        IntStream.rangeClosed(1, max).forEach(this::record);
        try {
            //connection.close();
            //channel.close();
            client.shutdown();
            latch.countDown();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
