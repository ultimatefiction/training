package com.veritas.bullshit.training.rbt;

public class Lock
{
    String nextLeg;
    Lock (String name)
    {
        nextLeg = name;
    }
    void next()
    {
        if (nextLeg.equals("Right"))
        {
            nextLeg = "Left";
        }
        else
        {
            nextLeg = "Right";
        }
    }
}
