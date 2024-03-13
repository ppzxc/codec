package io.github.ppzxc.codec.model;

public abstract class AbstractMessage implements Message {

  public static final int MINIMUM_MESSAGE_LENGTH = Header.HEADER_LENGTH + Header.MINIMUM_BODY_LENGTH;
  public static final byte[] LINE_DELIMITER = new byte[]{'\r', '\n'};
  private static final long serialVersionUID = -6149098856785467650L;
  protected final Header header;

  protected AbstractMessage(Header header) {
    this.header = header;
  }
}
