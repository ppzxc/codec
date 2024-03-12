package io.github.ppzxc.codec.model;

import java.io.Serializable;

/**
 * The interface Message.
 */
public interface Message extends Serializable {

  /**
   * Header header.
   *
   * @return the header
   */
  Header header();
}
