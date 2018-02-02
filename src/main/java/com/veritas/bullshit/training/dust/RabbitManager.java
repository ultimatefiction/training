package com.veritas.bullshit.training.dust;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitManager {

    private Connection connection;

    private String queueName;
    private String host;
    private String login;
    private String password;

    RabbitManager() {
    }

    public void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(login);
        factory.setPassword(password);
        connection = factory.newConnection();
    }

    public void initDefault(String queueName) throws IOException, TimeoutException {
        this.queueName = queueName;
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
        channel.queueDeclare(queueName, false, false, false, null);
        return channel;
    }

    public String getQueueName() {
        return queueName;
    }

    public RabbitManager setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public String getHost() {
        return host;
    }

    public RabbitManager setHost(String host) {
        this.host = host;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public RabbitManager setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RabbitManager setPassword(String password) {
        this.password = password;
        return this;
    }
}
