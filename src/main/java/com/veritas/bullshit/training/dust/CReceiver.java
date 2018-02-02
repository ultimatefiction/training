package com.veritas.bullshit.training.dust;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CReceiver extends Thread{

    private RabbitManager rabbitManager;
    private RedisManager redisManager;

    CReceiver(RabbitManager rabbitManager, RedisManager redisManager) {
        this.rabbitManager = rabbitManager;
        this.redisManager = redisManager;
    }

    @Override
    public void run() {
        System.out.print("[o] Receiver initialized\n");
        try (Channel channel = rabbitManager.getChannel()) {
            RScoredSortedSet set = redisManager.getSet();
            RLock lock = redisManager.getLock();
            Consumer consumer = new CConsumer(channel, rabbitManager.getQueueName(), set, lock);
            channel.basicConsume(rabbitManager.getQueueName(), true, consumer);
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

}
