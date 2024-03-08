package io.github.ppzxc.codec.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.github.ppzxc.codec.model.EncryptionMethod.Builder;
import java.io.Serializable;

/**
 * The type Encryption method.
 */
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

  /**
   * Gets type.
   *
   * @return the type
   */
  public EncryptionType getType() {
    return type;
  }

  /**
   * Gets mode.
   *
   * @return the mode
   */
  public EncryptionMode getMode() {
    return mode;
  }

  /**
   * Gets padding.
   *
   * @return the padding
   */
  public EncryptionPadding getPadding() {
    return padding;
  }

  /**
   * Gets iv.
   *
   * @return the iv
   */
  public String getIv() {
    return iv;
  }

  /**
   * Gets symmetric key.
   *
   * @return the symmetric key
   */
  public String getSymmetricKey() {
    return symmetricKey;
  }

  /**
   * Builder builder.
   *
   * @return the builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * The type Builder.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private EncryptionType type;
    private EncryptionMode mode;
    private EncryptionPadding padding;
    private String iv;
    private String symmetricKey;

    private Builder() {
    }

    /**
     * Type builder.
     *
     * @param type the type
     * @return the builder
     */
    public Builder type(EncryptionType type) {
      this.type = type;
      return this;
    }

    /**
     * Mode builder.
     *
     * @param mode the mode
     * @return the builder
     */
    public Builder mode(EncryptionMode mode) {
      this.mode = mode;
      return this;
    }

    /**
     * Padding builder.
     *
     * @param padding the padding
     * @return the builder
     */
    public Builder padding(EncryptionPadding padding) {
      this.padding = padding;
      return this;
    }

    /**
     * Iv builder.
     *
     * @param iv the iv
     * @return the builder
     */
    public Builder iv(String iv) {
      this.iv = iv;
      return this;
    }

    /**
     * Symmetric key builder.
     *
     * @param symmetricKey the symmetric key
     * @return the builder
     */
    public Builder symmetricKey(String symmetricKey) {
      this.symmetricKey = symmetricKey;
      return this;
    }

    /**
     * Build encryption method.
     *
     * @return the encryption method
     */
    public EncryptionMethod build() {
      return new EncryptionMethod(type, mode, padding, iv, symmetricKey);
    }
  }
}
