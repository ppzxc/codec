package io.github.ppzxc.codec.model;

import java.io.Serializable;

public class Header implements Serializable {

  public static final int MINIMUM_BODY_LENGTH = 2;
  public static final int HEADER_LENGTH = 12;
  private static final long serialVersionUID = 5464917518865088432L;
  private final int id;
  private final byte type;
  private final byte status;
  private final byte encoding;
  private final byte reserved;
  private final int bodyLength;

  private Header(int id, byte type, byte status, byte encoding, byte reserved, int bodyLength) {
    this.id = id;
    this.type = type;
    this.status = status;
    this.encoding = encoding;
    this.reserved = reserved;
    this.bodyLength = bodyLength;
  }

  public int getId() {
    return id;
  }

  public byte getType() {
    return type;
  }

  public byte getStatus() {
    return status;
  }

  public byte getEncoding() {
    return encoding;
  }

  public byte getReserved() {
    return reserved;
  }

  public int getBodyLength() {
    return bodyLength;
  }

  public static RawHeaderBuilder builder() {
    return new RawHeaderBuilder();
  }

  @Override
  public String toString() {
    return "Header{" +
      "id=" + id +
      ", type=" + type +
      ", status=" + status +
      ", encoding=" + encoding +
      ", reserved=" + reserved +
      ", bodyLength=" + bodyLength +
      '}';
  }

  public static final class RawHeaderBuilder {

    private int id;
    private byte type;
    private byte status;
    private byte encoding;
    private byte reserved;
    private int bodyLength;

    private RawHeaderBuilder() {
    }

    public RawHeaderBuilder id(int id) {
      this.id = id;
      return this;
    }

    public RawHeaderBuilder type(byte type) {
      this.type = type;
      return this;
    }

    public RawHeaderBuilder status(byte status) {
      this.status = status;
      return this;
    }

    public RawHeaderBuilder encoding(byte encoding) {
      this.encoding = encoding;
      return this;
    }

    public RawHeaderBuilder reserved(byte reserved) {
      this.reserved = reserved;
      return this;
    }

    public RawHeaderBuilder bodyLength(int bodyLength) {
      this.bodyLength = bodyLength;
      return this;
    }

    public Header build() {
      return new Header(id, type, status, encoding, reserved, bodyLength);
    }
  }
}
