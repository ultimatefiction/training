package com.veritas.bullshit.training.rabbies;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.redisson.Redisson;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver extends Thread {

    private int max;

    private Connection connection;
    private Channel channel;
    private Handler handler;

    private RedissonClient client;
    private RScoredSortedSet<String> set;

    private String queueName;
    private String host;
    private String login;
    private String setName;
    private String address;
    private String password;

    Receiver(int max, RConfig rConfig) {
        this.max = max;
        queueName = rConfig.getQueueName();
        host = rConfig.getHost();
        login = rConfig.getLogin();
        setName = rConfig.getSetName();
        address = rConfig.getAddress();
        password = rConfig.getPassword();
    }

    private void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(login);
        factory.setPassword(password);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);

        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password)
                .setDatabase(0);
        this.client = Redisson.create(config);
        this.set = client.getScoredSortedSet(setName);
        RReadWriteLock rwlock = client.getReadWriteLock("setLock");

        handler = new Handler(channel, queueName, setName, set, rwlock);

        System.out.print("[o] Receiver is initialized\n");
    }

    private void receive() {
        try {
            //channel.basicConsume(queueName, true, handler);
            GetResponse getResponse = channel.basicGet(queueName, true);
            String message = new String(getResponse.getBody(), "UTF-8");
            System.out.print(String.format("[<] Received report from %s: %s\n", queueName, message));
            String[] things = message.split(" --> ");
            System.out.print(String.format("[i] Extracted score (%s)\n", things[1]));
            set.add(Integer.parseInt(things[1]), message);
            System.out.print(String.format("[v] Message recorded to redis set '%s': %s\n", setName, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exit() throws IOException, TimeoutException {
        channel.close();
        connection.close();
        client.shutdown();

        System.out.print("[x] Receiver is finished working\n");
    }

    @Override
    public void run() {
        try {
            init();
            for (int i=0; i<max; i++) {
                System.out.print(String.format("[i] Attempting to receive #%s\n", i+1));
                receive();
            }
            exit();
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

}
