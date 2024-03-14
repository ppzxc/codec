package io.github.ppzxc.codec.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.github.ppzxc.codec.model.EncryptionMethod.Builder;
import java.io.Serializable;

@JsonDeserialize(builder = Builder.class)
public class EncryptionMethod implements Serializable {

  private static final long serialVersionUID = -3521225365392130685L;
  private final EncryptionType type;
  private final EncryptionMode mode;
  private final EncryptionPadding padding;
  private final String iv;
  private final String symmetricKey;

  private EncryptionMethod(EncryptionType type, EncryptionMode mode, EncryptionPadding padding, String iv,
    String symmetricKey) {
    this.type = type;
    this.mode = mode;
    this.padding = padding;
    this.iv = iv;
    this.symmetricKey = symmetricKey;
  }

  public EncryptionType getType() {
    return type;
  }

  public EncryptionMode getMode() {
    return mode;
  }

  public EncryptionPadding getPadding() {
    return padding;
  }

  public String getIv() {
    return iv;
  }

  public String getSymmetricKey() {
    return symmetricKey;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return "EncryptionMethod{" +
      "type=" + type +
      ", mode=" + mode +
      ", padding=" + padding +
      ", iv=" + iv +
      ", symmetricKey=" + symmetricKey +
      '}';
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private EncryptionType type;
    private EncryptionMode mode;
    private EncryptionPadding padding;
    private String iv;
    private String symmetricKey;

    private Builder() {
    }

    public Builder type(EncryptionType type) {
      this.type = type;
      return this;
    }

    public Builder mode(EncryptionMode mode) {
      this.mode = mode;
      return this;
    }

    public Builder padding(EncryptionPadding padding) {
      this.padding = padding;
      return this;
    }

    public Builder iv(String iv) {
      this.iv = iv;
      return this;
    }

    public Builder symmetricKey(String symmetricKey) {
      this.symmetricKey = symmetricKey;
      return this;
    }

    public EncryptionMethod build() {
      return new EncryptionMethod(type, mode, padding, iv, symmetricKey);
    }
  }
}
