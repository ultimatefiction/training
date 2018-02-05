package com.veritas.bullshit.training.rabbittutorials;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionProvider {

    private Connection connection;

    private String queueName;
    private String host;
    private String login;
    private String password;

    public ConnectionProvider() throws IOException, TimeoutException {
        initDefault();
    }

    public void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPassword(password);
        factory.setUsername(login);
        connection = factory.newConnection();
    }

    public void initDefault() throws IOException, TimeoutException {
        this.host = "localhost";
        this.login = "msxfusr";
        this.password = "msxfpwd";
        init();
    }

    public void exit() throws IOException {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }

    public Channel getChannel() throws IOException {
        Channel channel = connection.createChannel();
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        return channel;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
