package io.github.ppzxc.codec.mapper;

import io.github.ppzxc.codec.exception.DeserializeFailedProblemException;
import io.github.ppzxc.codec.exception.SerializeFailedProblemException;

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
   * @throws DeserializeFailedProblemException the deserialize failed problem exception
   */
  <T> T read(ReadCommand<T> command) throws DeserializeFailedProblemException;

  /**
   * Write byte [ ].
   *
   * @param command the command
   * @return the byte [ ]
   * @throws SerializeFailedProblemException the serialize failed problem exception
   */
  byte[] write(WriteCommand command) throws SerializeFailedProblemException;
}
