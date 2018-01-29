package com.veritas.bullshit.training.rabbies;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender extends Thread {

    private String queueName;

    Sender(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void run() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("msxfusr");
            factory.setPassword("msxfpwd");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(queueName, false, false, false, null);
            String message = "Hello from Sender!";
            channel.basicPublish("", queueName, null, message.getBytes());
            System.out.println("[x] -> Sent '" + message + "'");

            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
