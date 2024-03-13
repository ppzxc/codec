package io.github.ppzxc.codec.model;

/**
 * The type Abstract message.
 */
public abstract class AbstractMessage implements Message {

  /**
   * The constant MINIMUM_MESSAGE_LENGTH.
   */
  public static final int MINIMUM_MESSAGE_LENGTH = Header.HEADER_LENGTH + Header.MINIMUM_BODY_LENGTH;
  /**
   * The constant LINE_DELIMITER.
   */
  public static final byte[] LINE_DELIMITER = new byte[]{'\r', '\n'};
  private static final long serialVersionUID = -6149098856785467650L;
  /**
   * The Header.
   */
  protected final Header header;

  /**
   * Instantiates a new Abstract message.
   *
   * @param header the header
   */
  protected AbstractMessage(Header header) {
    this.header = header;
  }
}
