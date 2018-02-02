package com.veritas.bullshit.training.rabbittutorials.r1;

import com.veritas.bullshit.training.rabbittutorials.ConnectionProvider;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Launcher {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionProvider provider = new ConnectionProvider();
        provider.initDefault();

    }

}
