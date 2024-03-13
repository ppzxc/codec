package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedProblemException;
import io.github.ppzxc.codec.exception.SerializeFailedProblemException;

/**
 * The interface Mapper.
 */
public interface Mapper {

  /**
   * Read t.
   *
   * @param <T>     the type parameter
   * @param payload the payload
   * @param tClass  the t class
   * @return the t
   * @throws DeserializeFailedProblemException the deserialize failed problem exception
   */
  <T> T read(byte[] payload, Class<T> tClass) throws DeserializeFailedProblemException;

  /**
   * Write byte [ ].
   *
   * @param <T>     the type parameter
   * @param payload the payload
   * @return the byte [ ]
   * @throws SerializeFailedProblemException the serialize failed problem exception
   */
  <T> byte[] write(T payload) throws SerializeFailedProblemException;
}
