package com.veritas.bullshit.training.robot;

import java.util.stream.IntStream;

public class Robot {

    public static void main(String[] args) {

        int charge = 20;
        int legCount = 4;

        Lock lock = new Lock(legCount);

        IntStream.rangeClosed(1, legCount).forEach(i -> new Leg(i, charge, lock).start());

    }

}
