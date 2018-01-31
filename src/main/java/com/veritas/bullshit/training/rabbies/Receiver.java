package com.veritas.bullshit.training.rabbies;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

public class Receiver extends Thread {

    private int max;

    private Connection connection;
    private Channel channel;
    private Handler handler;

    private RedissonClient client;
    private RScoredSortedSet<String> set;

    private final String QUEUE_NAME = "sendingQueue";
    private final String SET_NAME = "senderMessages";
    private final String ADDRESS = "127.0.0.1:6379";;
    private final String HOST = "localhost";
    private final String LOGIN = "msxfusr";
    private final String PASSWORD = "msxfpwd";

    Receiver(int max) {
        this.max = max;
    }

    private void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(LOGIN);
        factory.setPassword(PASSWORD);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Config config = new Config();
        config.useSingleServer()
                .setAddress(ADDRESS)
                .setPassword(PASSWORD)
                .setDatabase(0);
        this.client = Redisson.create(config);
        this.set = client.getScoredSortedSet(SET_NAME);
        set.clear();

        handler = new Handler(channel, QUEUE_NAME, SET_NAME, set);

        System.out.print("[o] Receiver is initialized\n");
    }

    private void receive() {
        try {
            channel.basicConsume(QUEUE_NAME, true, handler);
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
                receive();
            }
            exit();
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

}
