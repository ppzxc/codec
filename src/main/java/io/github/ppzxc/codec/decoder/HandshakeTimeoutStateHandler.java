package io.github.ppzxc.codec.decoder;

import io.github.ppzxc.codec.model.CodecProblemCode;
import io.github.ppzxc.codec.model.HandshakeResult;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleUserEventChannelHandler;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandshakeTimeoutStateHandler extends SimpleUserEventChannelHandler<IdleStateEvent> {

  private static final Logger log = LoggerFactory.getLogger(HandshakeTimeoutStateHandler.class);
  private final long closeDelay;
  private final TimeUnit timeUnit;

  public HandshakeTimeoutStateHandler(long closeDelay, TimeUnit timeUnit) {
    this.closeDelay = closeDelay;
    this.timeUnit = timeUnit;
  }

  public HandshakeTimeoutStateHandler() {
    this.closeDelay = 1;
    this.timeUnit = TimeUnit.SECONDS;
  }

  @Override
  protected void eventReceived(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
    log.debug("{} id=[NO-ID] event={} triggered", ctx.channel(), evt.toString());
    switch (evt.state()) {
      case ALL_IDLE:
        log.info("{} id=[NO-ID] event={} no incoming and outgoing", ctx.channel(), evt);
        action(ctx, CodecProblemCode.HANDSHAKE_TIMEOUT_NO_BEHAVIOR);
        break;
      case READER_IDLE:
        log.info("{} id=[NO-ID] event={} no incoming", ctx.channel(), evt);
        action(ctx, CodecProblemCode.HANDSHAKE_TIMEOUT_NO_INCOMING);
        break;
      case WRITER_IDLE:
        log.info("{} id=[NO-ID] event={} no outgoing", ctx.channel(), evt);
        action(ctx, CodecProblemCode.HANDSHAKE_TIMEOUT_NO_OUTGOING);
        break;
    }
  }

  private void action(ChannelHandlerContext ctx, CodecProblemCode codecProblemCode) {
    ctx.writeAndFlush(HandshakeResult.of(codecProblemCode))
      .addListener(ignored -> ctx.executor().schedule(() -> ctx.close(), closeDelay, timeUnit));
  }
}
