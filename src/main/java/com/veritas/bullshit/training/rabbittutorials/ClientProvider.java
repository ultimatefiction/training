package com.veritas.bullshit.training.rabbittutorials;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.PrimitiveIterator;

public class ClientProvider {

    private Config config;

    private int dataBase;
    private String address;
    private String password;

    public ClientProvider() {
        initDefault();
    }

    public void init() {
        config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password)
                .setDatabase(dataBase);
    }

    public void initDefault() {
        this.dataBase = 0;
        this.address = "127.0.0.1:6379";
        this.password = "msxfpwd";
        init();
    }

    public RedissonClient getClient() {
        return Redisson.create(config);
    }

    public int getDataBase() {
        return dataBase;
    }

    public ClientProvider setDataBase(int dataBase) {
        this.dataBase = dataBase;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ClientProvider setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ClientProvider setPassword(String password) {
        this.password = password;
        return this;
    }
}
