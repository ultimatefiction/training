package com.veritas.bullshit.training.rbt;

public class Robot {

    public static void main(String ... args) {
        Lock lock = new Lock ("Right");
        new Foot("Left", lock).start();
        new Foot("Right", lock).start();
    }
}
