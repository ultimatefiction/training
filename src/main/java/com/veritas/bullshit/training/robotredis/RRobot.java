package com.veritas.bullshit.training.robotredis;

import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.stream.IntStream;

public class RRobot {

    public static void main(String[] args) {

        int charge = 10;
        int maxLegs = 2;
        Locker locker = new Locker(maxLegs);

        Config config = new Config();
        config.useSingleServer()
                .setAddress("127.0.0.1:6379")
                .setPassword("msxfpwd")
                .setDatabase(0);
        RedissonClient client = Redisson.create(config);
        RScoredSortedSet<String> set = client.getScoredSortedSet("lines");

        IntStream.rangeClosed(1, maxLegs).forEach(i -> new RLeg(i, charge, locker, set).start());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        set.readAll().forEach(System.out::print);

        client.shutdown();

    }

}
