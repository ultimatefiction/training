package com.veritas.bullshit.training.rabbies;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class Sender extends Thread {

    private int id;
    private int max;
    private Lock lock;
    private CountDownLatch latch;

    private Connection connection;
    private Channel channel;

    private final String QUEUE_NAME = "sendingQueue";
    private final String HOST = "localhost";
    private final String LOGIN = "msxfusr";
    private final String PASSWORD = "msxfpwd";


    Sender(int id, int max, Lock lock, CountDownLatch latch) {
        this.id = id;
        this.max = max;
        this.lock = lock;
        this.latch = latch;
    }

    private void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(LOGIN);
        factory.setPassword(PASSWORD);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.print(String.format("[o] Sender #%s is initialized\n", id));
    }

    private void process() throws IOException, InterruptedException {
        synchronized (lock) {
            if (lock.getCurrent() == id) {
                String message = String.format("%s --> %s\n", id, lock.getState());
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.print(String.format("[>] Sent report to %s: %s", QUEUE_NAME, message));
                lock.inc();
                lock.next();
                lock.notifyAll();
            } else {
                lock.wait();
            }
        }
    }

    private void exit() throws IOException, TimeoutException {
        channel.close();
        connection.close();
        latch.countDown();
        System.out.print(String.format("[x] Sender #%s is finished working\n", id));
    }

    @Override
    public void run() {
        try {
            init();
            while (lock.getState() <= max) {
                process();
            }
            exit();
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
