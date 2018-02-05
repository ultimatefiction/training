package com.veritas.bullshit.training.rabbittutorials.r2;

import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

public class RedisDisplayer {

    private RedissonClient client;
    private int maxWorkers;

    public RedisDisplayer(RedissonClient client, int maxWorkers) {
        this.client = client;
        this.maxWorkers = maxWorkers;
    }

    public void displayWorkerLogs() {
        for (int i=1; i<=maxWorkers; i++) {
            String workerName = "worker_" + i;
            RList<String> list = client.getList(workerName);
            System.out.printf("%n[L] Printing tasks of %s%n", workerName);
            list.readAll().forEach(System.out::println);
            list.delete();
        }
    }
}
