package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.ObjectUtils;

public abstract class AbstractProtocol implements Protocol {

  private static final long serialVersionUID = -6149098856785467650L;
  private final Header header;

  protected AbstractProtocol(Header header) {
    this.header = ObjectUtils.requireNonNull(header, new NullPointerException("header is null"));
  }

  @Override
  public Header header() {
    return header;
  }
}
