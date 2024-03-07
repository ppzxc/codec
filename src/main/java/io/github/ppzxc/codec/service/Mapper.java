package io.github.ppzxc.codec.service;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;

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
   * @throws DeserializeFailedException the deserialize failed exception
   */
  <T> T read(byte[] payload, Class<T> tClass) throws DeserializeFailedException;

  /**
   * Write byte [ ].
   *
   * @param <T>    the type parameter
   * @param object the object
   * @return the byte [ ]
   * @throws SerializeFailedException the serialize failed exception
   */
  <T> byte[] write(T object) throws SerializeFailedException;
}
