package com.veritas.bullshit.training.rabbittutorials.r2;

import com.rabbitmq.client.Channel;
import com.veritas.bullshit.training.rabbittutorials.ObjectByteWrapper;

import java.io.IOException;
import java.util.Random;

public class WorkDispenser extends Thread {

    private Channel channel;
    private String queueName;
    private int maxTasks;
    private int maxDifficulty;
    private int cooldownTime;

    public WorkDispenser(Channel channel, String queueName, int maxTasks, int maxDifficulty, int cooldownTime) {
        this.channel = channel;
        this.queueName = queueName;
        this.maxTasks = maxTasks;
        this.maxDifficulty = maxDifficulty;
        this.cooldownTime = cooldownTime;
    }

    private void publishTask(Data data) throws IOException {
        channel.basicPublish("", queueName, null, ObjectByteWrapper.getBytes(data));
        System.out.printf("[>] Sent task %s%n", data);
    }

    @Override
    public void run() {
        try {
            boolean durable = true;
            channel.queueDeclare(queueName, durable, false, false, null);
            Random random = new Random();
            for (int i=1; i<=maxTasks; i++) {
                int difficulty = random.nextInt(maxDifficulty);
                Data data = new Data("Task #" + i, difficulty + 1);
                publishTask(data);
                Thread.sleep(random.nextInt(cooldownTime) + 500);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
