package io.github.ppzxc.codec.model;

import java.io.Serializable;

/**
 * The type Encryption method.
 */
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
   * Builder encryption method builder.
   *
   * @return the encryption method builder
   */
  public static EncryptionMethodBuilder builder() {
    return new EncryptionMethodBuilder();
  }

  /**
   * The type Encryption method builder.
   */
  public static final class EncryptionMethodBuilder {

    private EncryptionType type;
    private EncryptionMode mode;
    private EncryptionPadding padding;
    private String iv;
    private String symmetricKey;

    private EncryptionMethodBuilder() {
    }

    /**
     * Type encryption method builder.
     *
     * @param type the type
     * @return the encryption method builder
     */
    public EncryptionMethodBuilder type(EncryptionType type) {
      this.type = type;
      return this;
    }

    /**
     * Mode encryption method builder.
     *
     * @param mode the mode
     * @return the encryption method builder
     */
    public EncryptionMethodBuilder mode(EncryptionMode mode) {
      this.mode = mode;
      return this;
    }

    /**
     * Padding encryption method builder.
     *
     * @param padding the padding
     * @return the encryption method builder
     */
    public EncryptionMethodBuilder padding(EncryptionPadding padding) {
      this.padding = padding;
      return this;
    }

    /**
     * Iv encryption method builder.
     *
     * @param iv the iv
     * @return the encryption method builder
     */
    public EncryptionMethodBuilder iv(String iv) {
      this.iv = iv;
      return this;
    }

    /**
     * Symmetric key encryption method builder.
     *
     * @param symmetricKey the symmetric key
     * @return the encryption method builder
     */
    public EncryptionMethodBuilder symmetricKey(String symmetricKey) {
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
