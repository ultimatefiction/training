package com.veritas.bullshit.training.robotredis;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

public class RRobot {

    public static void main(String[] args) throws IOException, TimeoutException {

        int charge = 4;
        int maxLegs = 2;
        Locker locker = new Locker(maxLegs);
        CountDownLatch latch = new CountDownLatch(maxLegs);

        Config config = new Config();
        config.useSingleServer()
                .setAddress("127.0.0.1:6379")
                .setPassword("msxfpwd")
                .setDatabase(0);
        RedissonClient client = Redisson.create(config);
        RScoredSortedSet<String> set = client.getScoredSortedSet("lines");
        set.clear();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("msxfusr");
        factory.setPassword("msxfpwd");
        Connection connection = factory.newConnection();

        IntStream.rangeClosed(1, maxLegs).forEach(i -> new RLeg(i, charge, locker, set, connection, latch).start());

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        set.readAll().forEach(System.out::print);

        client.shutdown();

    }

}
