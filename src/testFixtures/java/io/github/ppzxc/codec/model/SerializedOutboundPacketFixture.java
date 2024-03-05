package io.github.ppzxc.codec.model;

public final class SerializedOutboundPacketFixture {

  private SerializedOutboundPacketFixture() {
  }

  public static SerializedOutboundPacket create(Header header, byte[] body) {
    return SerializedOutboundPacket.builder()
      .header(header)
      .body(body)
      .build();
  }
}
