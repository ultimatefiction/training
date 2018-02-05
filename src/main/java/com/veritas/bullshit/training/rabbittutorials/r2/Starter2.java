package com.veritas.bullshit.training.rabbittutorials.r2;

import com.veritas.bullshit.training.rabbittutorials.ClientProvider;
import com.veritas.bullshit.training.rabbittutorials.ConnectionProvider;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Starter2 {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        ConnectionProvider cp = new ConnectionProvider();
        ClientProvider rp = new ClientProvider();
        String queueName = "task_queue";
        int maxTasks = 10;
        int maxDifficulty = 3;
        int maxWorkers = 3;
        int cooldownTime = 200;

        for (int i=1; i<=maxWorkers; i++) {
            new Worker(cp.getChannel(), rp.getClient(), queueName, i).start();
        }
        new WorkDispenser(cp.getChannel(), queueName, maxTasks, maxDifficulty, cooldownTime).start();

        Thread.sleep(maxTasks * maxDifficulty * 1000);

        new RedisDisplayer(rp.getClient(), maxWorkers).displayWorkerLogs();

    }

}
