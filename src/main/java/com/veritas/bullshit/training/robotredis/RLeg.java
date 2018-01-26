package com.veritas.bullshit.training.robotredis;

import org.redisson.api.RScoredSortedSet;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class RLeg extends Thread {

    private int name;
    private int charge;
    private Locker locker;
    private RScoredSortedSet<String> set;
    private CountDownLatch latch;

    RLeg(int name, int charge, Locker locker, RScoredSortedSet<String> set, CountDownLatch latch) {
        this.name = name;
        this.charge = charge;
        this.locker = locker;
        this.set = set;
        this.latch = latch;
        System.out.println(String.format(">> Leg #%s spawned!", name));
    }

    private void step(int count) {
        synchronized (locker) {
            if (locker.get() == this.name) {
                set.add(count, name + "-" + count + "\n");
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
        IntStream.rangeClosed(1, charge).forEach(this::step);
        latch.countDown();
    }

}