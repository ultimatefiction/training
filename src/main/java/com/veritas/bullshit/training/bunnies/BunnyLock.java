package com.veritas.bullshit.training.bunnies;

public class BunnyLock {

    private int total;
    private int current;
    private int state;

    BunnyLock(int total) {
        this.total = total;
        this.current = 1;
        this.state = 1;
        System.out.print(String.format("[x] Created lock for %s counters;\n", total));
    }

    public void next() {
        if (current != total) {
            current++;
        } else {
            current = 1;
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
