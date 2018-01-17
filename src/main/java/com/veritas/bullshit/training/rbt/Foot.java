package com.veritas.bullshit.training.rbt;

public class Foot extends Thread {
    private String name;
    private Lock lock;
    public Foot(String name, Lock lock)
    {
        this.lock = lock;
        this.name = name;
    }
    public void run()
    {
        while(true)
        {
            synchronized(lock)
            {
                if (name.equals(lock.nextLeg))
                {
                    System.out.println("Foot " + name);
                    lock.next();
                    lock.notifyAll();
                }
                else
                {
                    try {
                        lock.wait();
                    } catch (InterruptedException ex) { }
                }
            }
        }
    }
}
