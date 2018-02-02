package com.veritas.bullshit.training.dust;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisManager {

    private RedissonClient client;

    private int dataBase;
    private String address;
    private String password;
    private String setName;

    RedisManager() {
    }

    public void init() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password)
                .setDatabase(0);
        client = Redisson.create(config);
    }

    public void initDefault(String setName) {
        this.address = "127.0.0.1:6379";
        this.password = "msxfpwd";
        this.dataBase = 0;
        this.setName = setName;

        init();
    }

    public void exit() {
        client.shutdown();
    }

    public RScoredSortedSet<String> getSet() {
        return client.getScoredSortedSet(setName);
    }

    public RLock getLock() {
        return client.getReadWriteLock(setName + "lock").writeLock();
    }

    public String getAddress() {
        return address;
    }

    public RedisManager setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RedisManager setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSetName() {
        return setName;
    }

    public RedisManager setSetName(String setName) {
        this.setName = setName;
        return this;
    }

    public int getDataBase() {
        return dataBase;
    }

    public RedisManager setDataBase(int dataBase) {
        this.dataBase = dataBase;
        return this;
    }
}
