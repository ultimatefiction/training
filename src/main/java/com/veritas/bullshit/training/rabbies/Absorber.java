package com.veritas.bullshit.training.rabbies;

import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Absorber extends Thread {

    private RedissonClient client;
    private RScoredSortedSet<String> set;

    private String setName;
    private String address;
    private String password;

    Absorber(RConfig rConfig) {
        setName = rConfig.getSetName();
        address = rConfig.getAddress();
        password = rConfig.getPassword();
    }

    private void init() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password)
                .setDatabase(0);
        this.client = Redisson.create(config);
        this.set = client.getScoredSortedSet(setName);
        System.out.print("[o] Redis reader is initialized\n");
    }

    private void exit() {
        //set.clear();
        client.shutdown();
        System.out.print("[x] Redis reader is finished working\n");
    }

    @Override
    public void run() {
        init();

        System.out.print(String.format("[^] Reading messages from redis set '%s'\n", setName));
        set.readAll().forEach(System.out::println);

        exit();
    }

}
