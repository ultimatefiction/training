package com.veritas.bullshit.training.rabbittutorials.r2;

import java.io.Serializable;

public class Data implements Serializable{

    private String name;
    private int score;

    public Data(String name, int score) {
        this.name = name;
        this.score = score;
    }

    Data(String input) {
        String[] parts = input.split(" ");
        this.score = Integer.parseInt(parts[0]);
        this.name = parts[1];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("(%s) %s", score, name);
    }
}
