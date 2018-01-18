package com.veritas.bullshit.training.reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

/**
 * This program uses reflection to print all features of a class.
 * @version 1.1 2004-02-21
 * @author Cay Horstmann
 */
public class ReflectionTest
{

    public static void main(String[] args) {
        Clazz clazz = new Clazz();
        System.out.print(clazz);
    }

}
