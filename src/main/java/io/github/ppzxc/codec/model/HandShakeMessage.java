package io.github.ppzxc.codec.model;

/**
 * The type Hand shake message.
 */
public class HandShakeMessage extends AbstractMessage {

  /**
   * The constant HEADER_TYPE_CODE.
   */
  public static final byte HEADER_TYPE_CODE = 0x01;
  private static final long serialVersionUID = 7566973783972528091L;
  private final EncryptionMethod encryptionMethod;

  private HandShakeMessage(Header header, EncryptionMethod encryptionMethod) {
    super(header);
    this.encryptionMethod = encryptionMethod;
  }

  public Header header() {
    return header;
  }

  /**
   * Gets encryption method.
   *
   * @return the encryption method
   */
  public EncryptionMethod getEncryptionMethod() {
    return encryptionMethod;
  }

  /**
   * Builder hand shake builder.
   *
   * @return the hand shake builder
   */
  public static HandShakeBuilder builder() {
    return new HandShakeBuilder();
  }

  /**
   * The type Hand shake builder.
   */
  public static final class HandShakeBuilder {

    private Header header;
    private EncryptionMethod encryptionMethod;

    private HandShakeBuilder() {
    }

    /**
     * Header hand shake builder.
     *
     * @param header the header
     * @return the hand shake builder
     */
    public HandShakeBuilder header(Header header) {
      this.header = header;
      return this;
    }

    /**
     * Encryption method hand shake builder.
     *
     * @param encryptionMethod the encryption method
     * @return the hand shake builder
     */
    public HandShakeBuilder encryptionMethod(EncryptionMethod encryptionMethod) {
      this.encryptionMethod = encryptionMethod;
      return this;
    }

    /**
     * Build hand shake message.
     *
     * @return the hand shake message
     */
    public HandShakeMessage build() {
      return new HandShakeMessage(header, encryptionMethod);
    }
  }
}
