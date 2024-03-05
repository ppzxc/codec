package io.github.ppzxc.codec.model;

public final class DecryptedHandShakePacketFixture {

  private DecryptedHandShakePacketFixture() {
  }

  public static DecryptedHandShakePacket create(Header header, byte[] body) {
    return DecryptedHandShakePacket.builder()
      .header(header)
      .body(body)
      .build();
  }

  public static DecryptedHandShakePacket withBody(byte[] body) {
    return create(HeaderFixture.handShake(body.length), body);
  }
}
