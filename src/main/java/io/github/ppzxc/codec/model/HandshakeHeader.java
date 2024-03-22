package io.github.ppzxc.codec.model;

import io.github.ppzxc.codec.Constants;
import java.io.Serializable;

public class HandshakeHeader implements Serializable {

  public static final int LENGTH_FIELD_LENGTH = 4;
  public static final int PROTOCOL_FIELDS_LENGTH = 4;
  public static final int IV_PARAMETER_LENGTH = 16;
  public static final int SYMMETRIC_KEY_FIELD_MINIMUM_LENGTH = 16;
  public static final int MINIMUM_LENGTH =
    LENGTH_FIELD_LENGTH + PROTOCOL_FIELDS_LENGTH + IV_PARAMETER_LENGTH + SYMMETRIC_KEY_FIELD_MINIMUM_LENGTH
      + Constants.LineDelimiter.LENGTH;
  private static final long serialVersionUID = -7725222996410869058L;
  private final int length;
  private final HandshakeType handShakeType;
  private final EncryptionType encryptionType;
  private final EncryptionMode encryptionMode;
  private final EncryptionPadding encryptionPadding;

  public HandshakeHeader(int length, HandshakeType handShakeType, EncryptionType encryptionType,
    EncryptionMode encryptionMode, EncryptionPadding encryptionPadding) {
    this.length = length;
    this.handShakeType = handShakeType;
    this.encryptionType = encryptionType;
    this.encryptionMode = encryptionMode;
    this.encryptionPadding = encryptionPadding;
  }

  public int getLength() {
    return length;
  }

  public HandshakeType getHandShakeType() {
    return handShakeType;
  }

  public EncryptionType getEncryptionType() {
    return encryptionType;
  }

  public EncryptionMode getEncryptionMode() {
    return encryptionMode;
  }

  public EncryptionPadding getEncryptionPadding() {
    return encryptionPadding;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private int length;
    private HandshakeType handShakeType;
    private EncryptionType encryptionType;
    private EncryptionMode encryptionMode;
    private EncryptionPadding encryptionPadding;

    private Builder() {
    }

    public Builder length(int length) {
      this.length = length;
      return this;
    }

    public Builder handShakeType(HandshakeType handShakeType) {
      this.handShakeType = handShakeType;
      return this;
    }

    public Builder encryptionType(EncryptionType encryptionType) {
      this.encryptionType = encryptionType;
      return this;
    }

    public Builder encryptionMode(EncryptionMode encryptionMode) {
      this.encryptionMode = encryptionMode;
      return this;
    }

    public Builder encryptionPadding(EncryptionPadding encryptionPadding) {
      this.encryptionPadding = encryptionPadding;
      return this;
    }

    public HandshakeHeader build() {
      return new HandshakeHeader(length, handShakeType, encryptionType, encryptionMode, encryptionPadding);
    }
  }
}
