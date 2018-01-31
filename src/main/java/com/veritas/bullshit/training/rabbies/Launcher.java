package com.veritas.bullshit.training.rabbies;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class Launcher {

    public static void main(String[] args) {

        int senders = 3;
        int max = 9;
        Lock lock = new Lock(senders);
        CountDownLatch latch = new CountDownLatch(senders);

        IntStream.rangeClosed(1, senders).forEach(i -> new Sender(i, max, lock, latch).start());

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Receiver(max).start();

    }

}
