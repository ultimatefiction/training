package com.veritas.bullshit.training.bunnies;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

public class Counter extends Thread {

    private int index;
    private int max;
    private BunnyLock lock;
    private CountDownLatch latch;

    private RedissonClient client;
    private RScoredSortedSet<String> set;

    private Connection connection;
    private Channel channel;

    private final String QUEUE_NAME = "counterBuffer";
    private final String HOST = "localhost";
    private final String LOGIN = "msxfusr";
    private final String PASSWORD = "msxfpwd";

    Counter(int index, int max, BunnyLock lock, CountDownLatch latch) {
        this.index = index;
        this.max = max;
        this.lock = lock;
        this.latch = latch;

        Config config = new Config();
        config.useSingleServer()
                .setAddress("127.0.0.1:6379")
                .setPassword("msxfpwd")
                .setDatabase(0);
        this.client = Redisson.create(config);
        this.set = client.getScoredSortedSet("lines");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(LOGIN);
        factory.setPassword(PASSWORD);
        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

        System.out.print(String.format("--> Counter #%s started;\n", index));
    }

    private void step() {
        synchronized (lock) {
            if (lock.getCurrent() == index) {
                RLock rlock = client.getReadWriteLock("rwlock").writeLock();
                rlock.lock();
                set.add(lock.getState(), String.format("%s -> %s\n", index, lock.getState()));
                rlock.unlock();

                String message = String.format("%s -> %s\n", index, lock.getState());
                try {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.print("PUBLISHED: " + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lock.inc();
                lock.next();
                lock.notifyAll();
            } else {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        IntStream.rangeClosed(1, max)
                .forEach(count -> step());

        try {
            client.shutdown();
            //channel.close();
            //connection.close();
            latch.countDown();
        }  catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
