package com.veritas.bullshit.training.concurrency;

public class ParallelTest {

    public static void main(String[] args) {

        Account lupa = new Account("Lupa",300);
        Account pupa = new Account("Pupa", 300);

        AccountManager buhgalteriya1 = new AccountManager(lupa, pupa, 300);
        AccountManager buhgalteriya2 = new AccountManager(lupa, pupa, 200);
        AccountManager buhgalteriya3 = new AccountManager(pupa, lupa, 100);

        buhgalteriya1.run();
        buhgalteriya2.run();
        buhgalteriya3.run();

        System.out.print(lupa + "\n" + pupa);

    }

}
