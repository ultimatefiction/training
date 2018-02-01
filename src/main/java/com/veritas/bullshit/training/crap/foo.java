package com.veritas.bullshit.training.crap;

public class foo {

    public static void main(String[] args) {

        String message = "1 --> 2\n";
        String[] things = message.split(" --> ");
        int n = Integer.parseInt(things[1]);
        System.out.print("> " + n);

    }

}
