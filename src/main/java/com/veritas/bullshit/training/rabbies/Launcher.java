package com.veritas.bullshit.training.rabbies;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class Launcher {

    public static void main(String[] args) {

        int senders = 2;
        int max = 6;
        Lock lock = new Lock(senders);
        CountDownLatch latch = new CountDownLatch(senders);

        RConfig config = new RConfig();
        config
                .setAddress("127.0.0.1:6379")
                .setHost("localhost")
                .setLogin("msxfusr")
                .setPassword("msxfpwd")
                .setQueueName("sendingQueue")
                .setSetName("senderMessages");

        IntStream.rangeClosed(1, senders).forEach(i -> new Sender(i, max, lock, latch, config).start());

        try {
            latch.await();
            Thread writeSet = new Receiver(max, config);
            writeSet.start();
            writeSet.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Absorber(config).start();

    }

}
