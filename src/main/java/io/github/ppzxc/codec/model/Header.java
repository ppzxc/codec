package io.github.ppzxc.codec.model;

import java.io.Serializable;

public class Header implements Serializable {

  public static final int LENGTH_FIELD_LENGTH = 4;
  public static final int ID_FIELD_LENGTH = 8;
  public static final int PROTOCOL_FIELDS_LENGTH = 4;
  public static final int BODY_LENGTH = ID_FIELD_LENGTH + PROTOCOL_FIELDS_LENGTH + LineDelimiter.LENGTH;
  public static final int MINIMUM_LENGTH = LENGTH_FIELD_LENGTH + BODY_LENGTH;
  public static final int ENCRYPTED_EMPTY_FULL_LENGTH = 30;
  public static final int ENCRYPTED_EMPTY_BODY_LENGTH = ENCRYPTED_EMPTY_FULL_LENGTH - LENGTH_FIELD_LENGTH;
  private static final long serialVersionUID = 3858154716183837451L;
  private final int length;
  private final long id;
  private final byte type;
  private final byte status;
  private final byte encoding;
  private final byte reserved;

  public Header(int length, long id, byte type, byte status, byte encoding, byte reserved) {
    this.length = length;
    this.id = id;
    this.type = type;
    this.status = status;
    this.encoding = encoding;
    this.reserved = reserved;
  }

  public int getLength() {
    return length;
  }

  public long getId() {
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

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private int length;
    private long id;
    private byte type;
    private byte status;
    private byte encoding;
    private byte reserved;

    private Builder() {
    }

    public Builder length(int length) {
      this.length = length;
      return this;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder type(byte type) {
      this.type = type;
      return this;
    }

    public Builder status(byte status) {
      this.status = status;
      return this;
    }

    public Builder encoding(byte encoding) {
      this.encoding = encoding;
      return this;
    }

    public Builder reserved(byte reserved) {
      this.reserved = reserved;
      return this;
    }

    public Header build() {
      return new Header(length, id, type, status, encoding, reserved);
    }
  }
}
