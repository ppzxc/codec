package io.github.ppzxc.codec.model;

import java.io.Serializable;

/**
 * The type Encryption method.
 */
public class EncryptionMethod implements Serializable {

  private static final long serialVersionUID = -3521225365392130685L;
  private final String type;
  private final String mode;
  private final String pkcs;
  private final String iv;
  private final String symmetricKey;

  private EncryptionMethod(String type, String mode, String pkcs, String iv, String symmetricKey) {
    this.type = type;
    this.mode = mode;
    this.pkcs = pkcs;
    this.iv = iv;
    this.symmetricKey = symmetricKey;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Gets mode.
   *
   * @return the mode
   */
  public String getMode() {
    return mode;
  }

  /**
   * Gets pkcs.
   *
   * @return the pkcs
   */
  public String getPkcs() {
    return pkcs;
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

    private String type;
    private String mode;
    private String pkcs;
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
    public EncryptionMethodBuilder type(String type) {
      this.type = type;
      return this;
    }

    /**
     * Mode encryption method builder.
     *
     * @param mode the mode
     * @return the encryption method builder
     */
    public EncryptionMethodBuilder mode(String mode) {
      this.mode = mode;
      return this;
    }

    /**
     * Pkcs encryption method builder.
     *
     * @param pkcs the pkcs
     * @return the encryption method builder
     */
    public EncryptionMethodBuilder pkcs(String pkcs) {
      this.pkcs = pkcs;
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
      return new EncryptionMethod(type, mode, pkcs, iv, symmetricKey);
    }
  }
}
