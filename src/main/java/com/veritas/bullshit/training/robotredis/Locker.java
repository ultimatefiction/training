package com.veritas.bullshit.training.robotredis;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Locker {

    private int legsCount;
    private int current;

    public Lock lock = new ReentrantLock();
    public Condition lockMoved = lock.newCondition();

    Locker(int legsCount) {
        this.legsCount = legsCount;
        this.current = 1;
        System.out.print(String.format(">> Lock created for %s legs!\n", legsCount));
    }

    public int get() {
        return current;
    }

    public void next() {
        if (current < legsCount) {
            current += 1;
        } else {
            current = 1;
        }
        System.out.print(String.format(">> Lock moved to %s leg!\n", current));
    }

}
