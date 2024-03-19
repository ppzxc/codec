package io.github.ppzxc.codec.model;

public abstract class AbstractMessage implements Message {

  private static final long serialVersionUID = -6149098856785467650L;
  private final Header header;

  protected AbstractMessage(Header header) {
    this.header = header;
  }

  @Override
  public Header header() {
    return header;
  }
}
