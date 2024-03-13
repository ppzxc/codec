package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedProblemException;
import io.github.ppzxc.codec.exception.SerializeFailedProblemException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The type Java object mapper.
 */
public class JavaObjectMapper implements Mapper {

  private JavaObjectMapper() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T read(byte[] payload, Class<T> tClass) throws DeserializeFailedProblemException {
    try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(payload))) {
      return (T) objectInputStream.readObject();
    } catch (Exception throwable) {
      throw new DeserializeFailedProblemException(throwable);
    }
  }

  @Override
  public <T> byte[] write(T payload) throws SerializeFailedProblemException {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
      objectOutputStream.writeObject(payload);
      return byteArrayOutputStream.toByteArray();
    } catch (Exception throwable) {
      throw new SerializeFailedProblemException(throwable);
    }
  }

  /**
   * Create java object mapper.
   *
   * @return the java object mapper
   */
  public static JavaObjectMapper create() {
    return new JavaObjectMapper();
  }
}
