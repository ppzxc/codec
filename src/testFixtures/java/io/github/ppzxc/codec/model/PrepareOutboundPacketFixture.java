package io.github.ppzxc.codec.model;

public final class PrepareOutboundPacketFixture {

  private PrepareOutboundPacketFixture() {
  }

  public static PrepareOutboundPacket create(Header header, Object body) {
    return PrepareOutboundPacket.builder()
      .header(header)
      .body(body)
      .build();
  }

  public static PrepareOutboundPacket withBody(Object body) {
    return create(HeaderFixture.emptyBody(), body);
  }
}
