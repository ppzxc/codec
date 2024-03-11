package io.github.ppzxc.codec.model;

public final class OutboundMessageFixture {

  private OutboundMessageFixture() {
  }

  public static OutboundMessage create(Header header, Object body) {
    return OutboundMessage.builder()
      .header(header)
      .body(body)
      .build();
  }

  public static OutboundMessage withBody(Object body) {
    return create(HeaderFixture.emptyBody(), body);
  }
}
