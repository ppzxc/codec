package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaObjectMapper implements Mapper {

  private JavaObjectMapper() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T read(byte[] payload, Class<T> tClass) throws DeserializeFailedException {
    try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(payload))) {
      return (T) objectInputStream.readObject();
    } catch (Throwable throwable) {
      throw new DeserializeFailedException(throwable);
    }
  }

  @Override
  public <T> byte[] write(T payload) throws SerializeFailedException {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
      objectOutputStream.writeObject(payload);
      return byteArrayOutputStream.toByteArray();
    } catch (Throwable throwable) {
      throw new SerializeFailedException(throwable);
    }
  }

  public static JavaObjectMapper create() {
    return new JavaObjectMapper();
  }
}
