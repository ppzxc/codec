package io.github.ppzxc.codec.model;

public final class OutboundMessageFixture {

  private OutboundMessageFixture() {
  }

  public static OutboundProtocol create(Header header, Object body) {
    return OutboundProtocol.builder()
      .header(header)
      .body(body)
      .build();
  }
}
