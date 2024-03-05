package io.github.ppzxc.codec.model;

import java.io.Serializable;

/**
 * The interface Packet.
 */
public interface Packet extends Serializable {

  /**
   * Gets header.
   *
   * @return the header
   */
  Header getHeader();
}
