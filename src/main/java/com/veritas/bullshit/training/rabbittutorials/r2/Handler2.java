package com.veritas.bullshit.training.rabbittutorials.r2;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.veritas.bullshit.training.rabbittutorials.ObjectByteWrapper;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.io.IOException;
import java.util.Random;

public class Handler2 extends DefaultConsumer {

    private Random random;
    private RedissonClient client;
    private RList<String> tasklog;
    private int workerNumber;

    public Handler2(Channel channel, RedissonClient client, int workerNumber) {
        super(channel);
        this.workerNumber = workerNumber;
        this.client = client;
        random = new Random();
        tasklog = client.getList("worker_" + workerNumber);
    }

    private void doWork(Data data) throws InterruptedException {
        for (int i=1; i<=data.getScore(); i++) {
            Thread.sleep(random.nextInt(500) + 750);
            System.out.printf("[O] Working on '%s' [%s/%s]%n", data.getName(), i, data.getScore());
        }
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        try {
            Data data = (Data) ObjectByteWrapper.fromBytes(body);
            System.out.printf("[<] Task '%s' accepted by worker #%s%n", data.getName(), workerNumber);
            doWork(data);
            tasklog.add(data.toString());
            System.out.printf("[X] '%s' completed!%n", data.getName());
            super.getChannel().basicAck(envelope.getDeliveryTag(), false);
        } catch (InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
