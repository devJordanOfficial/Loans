package com.infamousgc.loans.Data;

import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ActionDataType implements PersistentDataType<byte[], Action> {
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Action> getComplexType() {
        return Action.class;
    }

    @Override
    public byte[] toPrimitive(Action complex, PersistentDataAdapterContext context) {
        return SerializationUtils.serialize(complex);
    }

    @Override
    public Action fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
        try {
            InputStream is = new ByteArrayInputStream(primitive);
            ObjectInputStream o = new ObjectInputStream(is);
            return (Action) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
