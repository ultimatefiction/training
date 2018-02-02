package com.veritas.bullshit.training.dust;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CLauncher {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        int maxMessages = 2;
        int senderCount = 2;
        int senderCooldownTime = 1000;

        RabbitManager rabbitManager = new RabbitManager();
        rabbitManager.initDefault("cQueue");

        RedisManager redisManager = new RedisManager();
        redisManager.initDefault("cMessages");

        for (int i=1; i<=senderCount; i++) {
            new CSender(i, maxMessages, senderCooldownTime, rabbitManager).start();
            Thread.sleep(senderCooldownTime / senderCount);
        }

        new CReceiver(rabbitManager, redisManager).start();

    }

}
