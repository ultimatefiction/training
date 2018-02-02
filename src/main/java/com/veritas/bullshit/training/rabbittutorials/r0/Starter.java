package com.veritas.bullshit.training.rabbittutorials.r0;

import com.veritas.bullshit.training.rabbittutorials.ClientProvider;
import com.veritas.bullshit.training.rabbittutorials.ConnectionProvider;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Starter {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        int maxMessages = 50;
        int sendersCount = 5;
        int cooldownTime = 5000;
        String queueName = "postbox";
        String setName = "letters";
        ConnectionProvider connectionProvider = new ConnectionProvider();
        ClientProvider clientProvider = new ClientProvider();

        new Receiver(queueName, setName, connectionProvider.getChannel(), clientProvider.getClient()).start();
        for (int i=1; i<=sendersCount; i++) {
            new Sender(queueName, connectionProvider.getChannel(), maxMessages, i, cooldownTime).start();
            Thread.sleep(cooldownTime / sendersCount);
        }

    }

}
