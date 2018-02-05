package com.veritas.bullshit.training.rabbittutorials;

import com.veritas.bullshit.training.rabbittutorials.r2.Data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ObjectByteWrapper {

    public ObjectByteWrapper() {
    }

    public static byte[] getBytes(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        out = new ObjectOutputStream(bos);
        out.writeObject(object);
        out.flush();
        byte[] bytes = bos.toByteArray();
        bos.close();
        return bytes;
    }

    public static Object fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in;
        in = new ObjectInputStream(bis);
        Object object = in.readObject();
        in.close();
        return object;
    }
}
