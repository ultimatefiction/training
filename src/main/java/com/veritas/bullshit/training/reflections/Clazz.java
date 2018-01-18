package com.veritas.bullshit.training.reflections;

import com.veritas.bullshit.training.concurrency.Account;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Clazz extends Closs {

    private int intValue;
    private String stringValue;
    private Account objectValue;

    public Clazz(int intValue, String stringValue, Account objectValue) {
        super();
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.objectValue = objectValue;
    }

    Clazz() {
        super();
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Account getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(Account objectValue) {
        this.objectValue = objectValue;
    }

    @Override
    public String toString() {

        Class c = this.getClass();
        Class superC = c.getSuperclass();

        String classModifiers = Modifier.toString(c.getModifiers());
        if (classModifiers.length() > 0) {
            classModifiers += " ";
        }

        String superClass = "";
        if (superC != null && superC != Object.class) {
            superClass = " extends " + superC.getName();
        }

        return classModifiers +
                c.getName() +
                superClass +
                " {\n\n" +
                stringFields(c) +
                "\n" +
                stringConstructors(c) +
                "\n" +
                stringMethods(c) +
                "\n}";

    }

    private String stringConstructors(Class c) {

        Constructor[] constructors = c.getDeclaredConstructors();
        StringBuilder sConstructor = new StringBuilder();

        for (Constructor constr : constructors) {

            String name = constr.getName();
            String modifiers = Modifier.toString(constr.getModifiers());
            if (modifiers.length() > 0) {
                modifiers += " ";
            }

            Class[] paramTypes = constr.getParameterTypes();
            StringBuilder params = new StringBuilder();
            generateParams(params, paramTypes);

            sConstructor
                    .append("    ")
                    .append(modifiers)
                    .append(name)
                    .append(" (")
                    .append(params)
                    .append(");\n");

        }

        return sConstructor.toString();

    }

    private String stringMethods(Class c) {

        Method[] methods = c.getDeclaredMethods();
        StringBuilder sMethods = new StringBuilder();

        for (Method method : methods) {

            String name = method.getName();
            String modifiers = Modifier.toString(method.getModifiers());
            if (modifiers.length() > 0) {
                modifiers += " ";
            }
            String returnType = method.getReturnType().getName();

            StringBuilder sParams = new StringBuilder();
            Class[] params = method.getParameterTypes();
            generateParams(sParams, params);

            sMethods
                    .append("    ")
                    .append(modifiers)
                    .append(name)
                    .append(" (")
                    .append(sParams)
                    .append(");\n");

        }

        return sMethods.toString();

    }

    private String stringFields(Class c) {

        Field[] fields = c.getDeclaredFields();
        StringBuilder sFields = new StringBuilder();

        for (Field field : fields) {
            String modifiers = Modifier.toString(field.getModifiers());
            if (modifiers.length() > 0) {
                modifiers += " ";
            }
            sFields
                    .append("    ")
                    .append(modifiers)
                    .append(field.getName())
                    .append("\n");
        }

        return sFields.toString();

    }

    private void generateParams(StringBuilder sParams, Class[] params) {
        boolean putComma = false;
        for (Class param : params) {
            if (putComma) {
                sParams.append(", ");
            }
            sParams.append(param.getName());
            putComma = true;
        }
    }

}
