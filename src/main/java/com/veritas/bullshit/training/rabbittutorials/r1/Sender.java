package com.veritas.bullshit.training.rabbittutorials.r1;

import com.rabbitmq.client.Channel;
import com.veritas.bullshit.training.rabbittutorials.ConnectionProvider;


public class Sender extends Thread {

    private ConnectionProvider provider;
    private String queueName;
    private int maxMessages;
    private int cooldownTime;

    public Sender(ConnectionProvider provider, String queueName, int maxMessages, int cooldownTime) {
        this.provider = provider;
        this.queueName = queueName;
        this.maxMessages = maxMessages;
        this.cooldownTime = cooldownTime;
    }

    @Override
    public void run() {
        //Channel channel = provider.getChannel();
        //channel.queueDeclare(queueName, false, false, false, null);
        for (int i=1; i<=maxMessages; i++) {
            //channel.basicPublish();
        }
    }
}
