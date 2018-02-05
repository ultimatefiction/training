package com.veritas.bullshit.training.rabbittutorials.r2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import com.veritas.bullshit.training.rabbittutorials.ConnectionProvider;
import com.veritas.bullshit.training.rabbittutorials.ObjectByteWrapper;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Starter2 {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, ClassNotFoundException {

        ConnectionProvider cp = new ConnectionProvider();
        Channel channel = cp.getChannel();
        String queueName = "datasend";
        channel.queueDeclare(queueName, false, false, false, null);

        Data sentData = new Data("Foobar", 1);
        channel.basicPublish("", queueName, null, ObjectByteWrapper.getBytes(sentData));
        System.out.printf("[>] Sent message: '%s'%n", sentData);
        Thread.sleep(500);

        GetResponse response = channel.basicGet(queueName, true);
        Data receivedData = (Data) ObjectByteWrapper.fromBytes(response.getBody());
        System.out.printf("[<] Received message: '%s'%n", receivedData);

    }

}
