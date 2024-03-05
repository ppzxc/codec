package io.github.ppzxc.codec.model;

public final class EncryptedOutboundPacketFixture {

  private EncryptedOutboundPacketFixture() {
  }

  public static EncryptedOutboundPacket create(Header header, byte[] body) {
    return EncryptedOutboundPacket.builder()
      .header(header)
      .body(body)
      .build();
  }
}
