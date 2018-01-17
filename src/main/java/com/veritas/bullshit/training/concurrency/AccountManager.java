package com.veritas.bullshit.training.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountManager implements Runnable {

    private Account to;
    private Account from;
    private int sum;

    private Lock lock;

    AccountManager(Account to, Account from, int sum) {
        this.to = to;
        this.from = from;
        this.sum = sum;
        this.lock = new ReentrantLock();
    }

    private void transaction() {
        try {
            if(lock.tryLock(10, TimeUnit.SECONDS))
            System.out.print(String.format("> Transaction: from %s to %s (%s) -- ", from.getName(), to.getName(), sum));
            if (from.getBalance() - sum >= 0) {
                to.add(sum);
                from.substract(sum);
                System.out.print("Successful!\n");
            } else {
                System.out.print(String.format("Error: not enough money on %s's account!\n", from.getName()));
            }
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }  finally {
            lock.unlock();
        }
    }

    public void run() {
        transaction();
    }
}
