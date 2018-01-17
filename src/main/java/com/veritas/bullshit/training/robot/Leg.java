package com.veritas.bullshit.training.robot;

import java.util.stream.IntStream;

public class Leg extends Thread {

    private int name;
    private int charge;
    private final Lock lock;

    Leg(int name, int charge, Lock lock) {
        this.name = name;
        this.charge = charge;
        this.lock = lock;
        System.out.print(String.format(">> Leg #%s spawned!\n", name));
    }

    private void step(int count) {
        synchronized (lock) {
            if (lock.get() == this.name) {
                System.out.print(String.format("/\\ %s %s-leg!\n", count, name));
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
        IntStream.rangeClosed(1, charge).forEach(this::step);
    }

}
