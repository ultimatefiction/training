package com.veritas.bullshit.training.dust;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CSender extends Thread {

     private int id;
     private int maxMessages;
     private int cooldownTime;
     private RabbitManager manager;

    CSender(int id, int maxMessages, int cooldownTime, RabbitManager manager) {
        this.id = id;
        this.maxMessages = maxMessages;
        this.cooldownTime = cooldownTime;
        this.manager = manager;
    }

    @Override
    public void run() {
        System.out.print(String.format("[o] Sender #%s initialized\n", id));
        try (Channel channel = manager.getChannel()) {
            for (int i=1; i<=maxMessages; i++) {
                String message = String.format("%s --> %s", id, i);
                channel.basicPublish("", manager.getQueueName(), null, message.getBytes());
                System.out.print(String.format("[>] Sent report to %s: %s\n", manager.getQueueName(), message));
                Thread.sleep(cooldownTime);
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
