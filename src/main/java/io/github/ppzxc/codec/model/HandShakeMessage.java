package io.github.ppzxc.codec.model;

public class HandShakeMessage extends AbstractMessage {

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

  public EncryptionMethod getEncryptionMethod() {
    return encryptionMethod;
  }

  public static HandShakeBuilder builder() {
    return new HandShakeBuilder();
  }

  public static final class HandShakeBuilder {

    private Header header;
    private EncryptionMethod encryptionMethod;

    private HandShakeBuilder() {
    }

    public HandShakeBuilder header(Header header) {
      this.header = header;
      return this;
    }

    public HandShakeBuilder encryptionMethod(EncryptionMethod encryptionMethod) {
      this.encryptionMethod = encryptionMethod;
      return this;
    }

    public HandShakeMessage build() {
      return new HandShakeMessage(header, encryptionMethod);
    }
  }
}
