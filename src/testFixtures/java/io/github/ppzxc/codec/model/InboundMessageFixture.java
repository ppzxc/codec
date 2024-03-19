package io.github.ppzxc.codec.model;

public final class InboundMessageFixture {

  private InboundMessageFixture() {
  }

  public static InboundMessage create(Header header, byte[] body) {
    return InboundMessage.builder()
      .header(header)
      .body(body)
      .build();
  }

  public static InboundMessage create(int length, byte[] body) {
    return create(HeaderFixture.create(length), body);
  }

  public static InboundMessage create(byte[] body) {
    return create(HeaderFixture.create(body.length + Header.LENGTH_WITHOUT_LENGTH_FIELD), body);
  }
}
