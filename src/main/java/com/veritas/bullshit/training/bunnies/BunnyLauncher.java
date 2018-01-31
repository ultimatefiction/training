package com.veritas.bullshit.training.bunnies;

import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class BunnyLauncher {

    public static void main(String[] args) {
        int counters = 3;
        int max = 10;

        BunnyLock lock = new BunnyLock(counters);
        CountDownLatch latch = new CountDownLatch(counters);

        Config config = new Config();
        config.useSingleServer()
                .setAddress("127.0.0.1:6379")
                .setPassword("msxfpwd")
                .setDatabase(0);
        RedissonClient client = Redisson.create(config);
        RScoredSortedSet<String> set = client.getScoredSortedSet("lines");

        IntStream.rangeClosed(1, counters)
                .forEach(i -> new Counter(i, max, lock, latch).start());

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new BunnyRecorder(max, latch).run();

        System.out.print("Publishing redis set's contents...\n========");
        set.readAll().forEach(System.out::print);

        client.shutdown();
    }

}
