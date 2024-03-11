package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedException;
import io.github.ppzxc.codec.exception.SerializeFailedException;

/**
 * The interface Multi mapper.
 */
public interface MultiMapper {

  /**
   * Read t.
   *
   * @param <T>     the type parameter
   * @param command the command
   * @return the t
   * @throws DeserializeFailedException the deserialize failed exception
   */
  <T> T read(ReadCommand<T> command) throws DeserializeFailedException;

  /**
   * Write byte [ ].
   *
   * @param command the command
   * @return the byte [ ]
   * @throws SerializeFailedException the serialize failed exception
   */
  byte[] write(WriteCommand command) throws SerializeFailedException;
}
