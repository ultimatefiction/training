package com.veritas.bullshit.training.rabbies;

public class RConfig {

    private String queueName;
    private String setName;
    private String address;
    private String host;
    private String login;
    private String password;

    RConfig() {
    }

    public String getQueueName() {
        return queueName;
    }

    public RConfig setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public String getSetName() {
        return setName;
    }

    public RConfig setSetName(String setName) {
        this.setName = setName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public RConfig setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getHost() {
        return host;
    }

    public RConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public RConfig setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RConfig setPassword(String password) {
        this.password = password;
        return this;
    }
}
