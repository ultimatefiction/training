package com.veritas.bullshit.training.robot;

public class Lock {

    private int legsCount;
    private int current;

    Lock(int legsCount) {
        this.legsCount = legsCount;
        this.current = 1;
        System.out.print(String.format(">> Lock created for %s legs!\n", legsCount));
    }

    public int get() {
        return current;
    }

    public void next() {
        if (current < legsCount) {
            current += 1;
        } else {
            current = 1;
        }
        System.out.print(String.format(">> Lock moved to %s leg!\n", current));
    }


}
