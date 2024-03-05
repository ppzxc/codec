package io.github.ppzxc.codec.model;

import java.io.Serializable;

/**
 * The type Header.
 */
public class Header implements Serializable {

  /**
   * The constant MINIMUM_BODY_LENGTH.
   */
  public static final int MINIMUM_BODY_LENGTH = 2;
  /**
   * The constant HEADER_LENGTH.
   */
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

  /**
   * Gets id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public byte getType() {
    return type;
  }

  /**
   * Gets status.
   *
   * @return the status
   */
  public byte getStatus() {
    return status;
  }

  /**
   * Gets encoding.
   *
   * @return the encoding
   */
  public byte getEncoding() {
    return encoding;
  }

  /**
   * Gets reserved.
   *
   * @return the reserved
   */
  public byte getReserved() {
    return reserved;
  }

  /**
   * Gets body length.
   *
   * @return the body length
   */
  public int getBodyLength() {
    return bodyLength;
  }

  /**
   * Builder raw header builder.
   *
   * @return the raw header builder
   */
  public static RawHeaderBuilder builder() {
    return new RawHeaderBuilder();
  }

  /**
   * The type Raw header builder.
   */
  public static final class RawHeaderBuilder {

    private int id;
    private byte type;
    private byte status;
    private byte encoding;
    private byte reserved;
    private int bodyLength;

    private RawHeaderBuilder() {
    }

    /**
     * Id raw header builder.
     *
     * @param id the id
     * @return the raw header builder
     */
    public RawHeaderBuilder id(int id) {
      this.id = id;
      return this;
    }

    /**
     * Type raw header builder.
     *
     * @param type the type
     * @return the raw header builder
     */
    public RawHeaderBuilder type(byte type) {
      this.type = type;
      return this;
    }

    /**
     * Status raw header builder.
     *
     * @param status the status
     * @return the raw header builder
     */
    public RawHeaderBuilder status(byte status) {
      this.status = status;
      return this;
    }

    /**
     * Encoding raw header builder.
     *
     * @param encoding the encoding
     * @return the raw header builder
     */
    public RawHeaderBuilder encoding(byte encoding) {
      this.encoding = encoding;
      return this;
    }

    /**
     * Reserved raw header builder.
     *
     * @param reserved the reserved
     * @return the raw header builder
     */
    public RawHeaderBuilder reserved(byte reserved) {
      this.reserved = reserved;
      return this;
    }

    /**
     * Body length raw header builder.
     *
     * @param bodyLength the body length
     * @return the raw header builder
     */
    public RawHeaderBuilder bodyLength(int bodyLength) {
      this.bodyLength = bodyLength;
      return this;
    }

    /**
     * Build header.
     *
     * @return the header
     */
    public Header build() {
      return new Header(id, type, status, encoding, reserved, bodyLength);
    }
  }
}
