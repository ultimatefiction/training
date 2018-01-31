package com.veritas.bullshit.training.rabbies;

public class Lock {

    private int total;
    private int current;
    private int state;

    Lock(int total) {
        this.total = total;
        this.current = 1;
        this.state = 1;
        System.out.print(String.format("[i] Lock configured for %s threads\n", total));
    }

    public void next() {
        if (current == total) {
            current = 1;
        } else {
            current++;
        }
    }

    public void inc() {
        state++;
    }

    public int getCurrent() {
        return current;
    }

    public int getState() {
        return state;
    }
}
