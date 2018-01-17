package com.veritas.bullshit.training.concurrency;

public class Account {

    private String name;
    private int balance;

    public Account(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void add(int sum) {
        balance += sum;
    }

    public void substract(int sum) {
        balance -= sum;
    }

    public String toString() {
        return String.format("%s: %s", name, balance);
    }
}
