package com.veritas.bullshit.training.rabbies;

public class Launcher {

    public static void main(String[] args) {

        String queueName = "hello";
        new Sender(queueName).start();
        new Receiver(queueName).start();

    }

}
