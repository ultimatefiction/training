package com.veritas.bullshit.training.robotredis;

import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class CheckingClass {

    public static void check() {

        Config config = new Config();
        config.useSingleServer()
                .setAddress("127.0.0.1:6379")
                .setPassword("msxfpwd")
                .setDatabase(0);
        RedissonClient client = Redisson.create(config);
        RScoredSortedSet<String> set = client.getScoredSortedSet("lines");

        String chstr = "This is our set!";
        System.out.print("CHECKING: " + set.contains(chstr) + " --> " + set.getScore(chstr));

        client.shutdown();

    }

}
