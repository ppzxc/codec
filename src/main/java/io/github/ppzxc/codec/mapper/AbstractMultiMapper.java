package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedProblemException;
import io.github.ppzxc.codec.exception.SerializeFailedProblemException;

/**
 * The type Abstract multi mapper.
 */
public abstract class AbstractMultiMapper implements MultiMapper {

  /**
   * Read from proto buf t.
   *
   * @param <T>     the type parameter
   * @param payload the payload
   * @param tClass  the t class
   * @return the t
   * @throws DeserializeFailedProblemException the deserialize failed problem exception
   */
  abstract <T> T readFromProtoBuf(byte[] payload, Class<T> tClass) throws DeserializeFailedProblemException;

  /**
   * Read from json t.
   *
   * @param <T>     the type parameter
   * @param payload the payload
   * @param tClass  the t class
   * @return the t
   * @throws DeserializeFailedProblemException the deserialize failed problem exception
   */
  abstract <T> T readFromJson(byte[] payload, Class<T> tClass) throws DeserializeFailedProblemException;

  /**
   * Read from bson t.
   *
   * @param <T>     the type parameter
   * @param payload the payload
   * @param tClass  the t class
   * @return the t
   * @throws DeserializeFailedProblemException the deserialize failed problem exception
   */
  abstract <T> T readFromBson(byte[] payload, Class<T> tClass) throws DeserializeFailedProblemException;

  /**
   * Read from java t.
   *
   * @param <T>     the type parameter
   * @param payload the payload
   * @param tClass  the t class
   * @return the t
   * @throws DeserializeFailedProblemException the deserialize failed problem exception
   */
  abstract <T> T readFromJava(byte[] payload, Class<T> tClass) throws DeserializeFailedProblemException;

  /**
   * Write using proto buf byte [ ].
   *
   * @param <T>     the type parameter
   * @param payload the payload
   * @return the byte [ ]
   * @throws SerializeFailedProblemException the serialize failed problem exception
   */
  abstract <T> byte[] writeUsingProtoBuf(T payload) throws SerializeFailedProblemException;

  /**
   * Write using json byte [ ].
   *
   * @param <T>     the type parameter
   * @param payload the payload
   * @return the byte [ ]
   * @throws SerializeFailedProblemException the serialize failed problem exception
   */
  abstract <T> byte[] writeUsingJson(T payload) throws SerializeFailedProblemException;

  /**
   * Write using bson byte [ ].
   *
   * @param <T>     the type parameter
   * @param payload the payload
   * @return the byte [ ]
   * @throws SerializeFailedProblemException the serialize failed problem exception
   */
  abstract <T> byte[] writeUsingBson(T payload) throws SerializeFailedProblemException;

  /**
   * Write using java byte [ ].
   *
   * @param <T>     the type parameter
   * @param payload the payload
   * @return the byte [ ]
   * @throws SerializeFailedProblemException the serialize failed problem exception
   */
  abstract <T> byte[] writeUsingJava(T payload) throws SerializeFailedProblemException;

  @Override
  public <T> T read(ReadCommand<T> command) throws DeserializeFailedProblemException {
    switch (command.getType()) {
      case PROTOBUF:
        return readFromProtoBuf(command.getPayload(), command.getTargetClass());
      case JSON:
        return readFromJson(command.getPayload(), command.getTargetClass());
      case BSON:
        return readFromBson(command.getPayload(), command.getTargetClass());
      case JAVA_BINARY:
        return readFromJava(command.getPayload(), command.getTargetClass());
      case NULL:
      default:
        throw new DeserializeFailedProblemException("not found encoding type");
    }
  }

  @Override
  public byte[] write(WriteCommand command) throws SerializeFailedProblemException {
    switch (command.getType()) {
      case PROTOBUF:
        return writeUsingProtoBuf(command.getPayload());
      case JSON:
        return writeUsingJson(command.getPayload());
      case BSON:
        return writeUsingBson(command.getPayload());
      case JAVA_BINARY:
        return writeUsingJava(command.getPayload());
      case NULL:
      default:
        throw new SerializeFailedProblemException("not found encoding type");
    }
  }
}
