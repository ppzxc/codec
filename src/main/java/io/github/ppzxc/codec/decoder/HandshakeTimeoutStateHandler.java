package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.model.CodecProblemCode;
import io.github.ppzxc.codec.model.HandshakeResult;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleUserEventChannelHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandshakeTimeoutStateHandler extends SimpleUserEventChannelHandler<IdleStateEvent> {

  private static final Logger log = LoggerFactory.getLogger(HandshakeTimeoutStateHandler.class);
  private final int readerTimeoutSeconds;
  private final int writerTimeoutSeconds;
  private final int allTimeoutSeconds;

  public HandshakeTimeoutStateHandler(int readerTimeoutSeconds, int writerTimeoutSeconds, int allTimeoutSeconds) {
    this.readerTimeoutSeconds = readerTimeoutSeconds;
    this.writerTimeoutSeconds = writerTimeoutSeconds;
    this.allTimeoutSeconds = allTimeoutSeconds;
  }

  public HandshakeTimeoutStateHandler() {
    this.readerTimeoutSeconds = 15;
    this.writerTimeoutSeconds = 10;
    this.allTimeoutSeconds = 5;
  }

  @Override
  protected void eventReceived(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
    log.debug("{} id=[NO-ID] event={} triggered", ctx.channel(), evt.toString());
    if (isContainsTimeoutEvent(evt)) {
      log.info("{} id=[NO-ID] event={} reader={} writer={} all={} no incoming handshake", ctx.channel(), evt,
        readerTimeoutSeconds, writerTimeoutSeconds, allTimeoutSeconds);
      ctx.writeAndFlush(HandshakeResult.of(CodecProblemCode.HANDSHAKE_TIMEOUT_NO_BEHAVIOR))
        .addListener(ignored -> ctx.close());
    }
  }

  private boolean isContainsTimeoutEvent(IdleStateEvent evt) {
    return evt == IdleStateEvent.ALL_IDLE_STATE_EVENT || evt == IdleStateEvent.READER_IDLE_STATE_EVENT
      || evt == IdleStateEvent.WRITER_IDLE_STATE_EVENT;
  }
}
