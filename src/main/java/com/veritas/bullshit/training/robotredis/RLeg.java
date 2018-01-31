package com.veritas.bullshit.training.robotredis;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.redisson.api.RScoredSortedSet;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class RLeg extends Thread {

    private int name;
    private int charge;
    private final Locker locker;
    private RScoredSortedSet<String> set;
    private Connection connection;
    private CountDownLatch latch;

    RLeg(int name, int charge, Locker locker, RScoredSortedSet<String> set, Connection connection, CountDownLatch latch) {
        this.name = name;
        this.charge = charge;
        this.locker = locker;
        this.set = set;
        this.connection = connection;
        this.latch = latch;
        System.out.println(String.format(">> Leg #%s spawned!", name));
    }

    private void step(int count) {
        synchronized (locker) {
            if (locker.get() == this.name) {
                set.add(count, name + "-" + count + "\n");
                System.out.print(name + "-" + count + "\n");
                locker.next();
                locker.notifyAll();
            } else {
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        IntStream.rangeClosed(1, charge).filter(i -> i % name == 0).forEach(this::step);
        latch.countDown();
    }

}
