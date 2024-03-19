package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncodingType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaSerializeMapper implements Mapper {

  private JavaSerializeMapper() {
  }

  public static JavaSerializeMapper create() {
    return new JavaSerializeMapper();
  }

  @SuppressWarnings("all")
  @Override
  public <T> T read(ReadCommand<T> command) throws DeserializeFailedException {
    if (command.getType() != EncodingType.JAVA_SERIALIZE) {
      throw new IllegalArgumentException(command.getType().toString());
    }
    try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(command.getPayload()))) {
      return (T) objectInputStream.readObject();
    } catch (Exception throwable) {
      throw new DeserializeFailedException(throwable);
    }
  }

  @Override
  public byte[] write(WriteCommand command) throws SerializeFailedException {
    if (command.getType() != EncodingType.JAVA_SERIALIZE) {
      throw new IllegalArgumentException(command.getType().toString());
    }
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
      objectOutputStream.writeObject(command.getPayload());
      return byteArrayOutputStream.toByteArray();
    } catch (Exception throwable) {
      throw new SerializeFailedException(throwable);
    }
  }
}
