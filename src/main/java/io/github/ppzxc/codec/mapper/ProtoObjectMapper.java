package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;
import io.github.ppzxc.codec.model.EncryptionMethod;
import io.github.ppzxc.codec.model.EncryptionMode;
import io.github.ppzxc.codec.model.EncryptionPadding;
import io.github.ppzxc.codec.model.EncryptionType;
import io.github.ppzxc.codec.model.protobuf.EncryptionMethodProtobuf;

/**
 * The type Proto object mapper.
 */
public class ProtoObjectMapper implements Mapper {

  @SuppressWarnings("unchecked")
  @Override
  public <T> T read(byte[] payload, Class<T> tClass) throws DeserializeFailedException {
    if (tClass.equals(EncryptionMethod.class)) {
      try {
        EncryptionMethodProtobuf encryptionMethodProtobuf = EncryptionMethodProtobuf.parseFrom(payload);
        return (T) EncryptionMethod.builder()
          .type(EncryptionType.of(encryptionMethodProtobuf.getType().name()))
          .mode(EncryptionMode.of(encryptionMethodProtobuf.getMode().name()))
          .padding(EncryptionPadding.of(encryptionMethodProtobuf.getPadding().name()))
          .iv(encryptionMethodProtobuf.getIv())
          .symmetricKey(encryptionMethodProtobuf.getSymmetricKey())
          .build();
      } catch (Throwable throwable) {
        throw new DeserializeFailedException(throwable);
      }
    } else {
      throw new DeserializeFailedException();
    }
  }

  @Override
  public <T> byte[] write(T payload) throws SerializeFailedException {
    return new byte[0];
  }
}
