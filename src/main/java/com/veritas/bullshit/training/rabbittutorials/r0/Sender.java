package com.veritas.bullshit.training.rabbittutorials.r0;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

public class Sender extends Thread{

    private String queueName;
    private Channel channel;
    private int maxMessages;
    private int number;
    private int cooldownTime;

    public Sender(String queueName, Channel channel, int maxMessages, int number, int cooldownTime) {
        this.queueName = queueName;
        this.channel = channel;
        this.maxMessages = maxMessages;
        this.number = number;
        this.cooldownTime = cooldownTime;
        System.out.println(String.format(" [*] Sender #%s was initialized", number));
    }

    private void send(int count) {
        try {
            String message = String.format("%s-%s", number, count);
            channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
            System.out.println(String.format(" [>] Sent message '%s' to %s", message, queueName));
            Thread.sleep(cooldownTime);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            channel.queueDeclare(queueName, false, false, false, null);
            IntStream.rangeClosed(1, maxMessages).forEach(this::send);
            channel.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
