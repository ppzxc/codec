package io.github.ppzxc.codec.service;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The type Object output stream mapper.
 */
@SuppressWarnings("unchecked")
public class ObjectOutputStreamMapper implements Mapper {

  @Override
  public <T> T read(byte[] payload, Class<T> tClass) throws DeserializeFailedException {
    try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(payload))) {
      return (T) objectInputStream.readObject();
    } catch (Exception e) {
      throw new DeserializeFailedException(e);
    }
  }

  @Override
  public <T> byte[] write(T object) throws SerializeFailedException {
//    if (object == null) {
//      throw new SerializeFailedException("object is require non null");
//    }
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
      objectOutputStream.writeObject(object);
      return byteArrayOutputStream.toByteArray();
    } catch (Exception e) {
      throw new SerializeFailedException(e);
    }
  }
}