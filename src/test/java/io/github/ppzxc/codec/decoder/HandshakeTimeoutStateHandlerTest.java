package io.github.ppzxc.codec.decoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.ppzxc.codec.model.CodecProblemCode;
import io.github.ppzxc.codec.model.HandshakeResult;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class HandshakeTimeoutStateHandlerTest {

  public static final ByteBuf EXPECTED = HandshakeResult.of(CodecProblemCode.HANDSHAKE_TIMEOUT_NO_BEHAVIOR);
  private HandshakeTimeoutStateHandler handler;

  @BeforeEach
  void setUp() {
    handler = new HandshakeTimeoutStateHandler();
  }

  @ParameterizedTest
  @MethodSource("use")
  void should_called_close(IdleStateEvent event) throws Exception {
    // given
    ChannelFuture future = mock(ChannelFuture.class);
    ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);

    // when
    when(ctx.writeAndFlush(eq(EXPECTED))).thenReturn(future);
    handler.eventReceived(ctx, event);

    // then
    verify(ctx, times(1)).writeAndFlush(any());
    verify(future, times(1)).addListener(any());
  }

  @ParameterizedTest
  @MethodSource("notUse")
  void should_not_called_close(IdleStateEvent event) throws Exception {
    // given
    ChannelFuture future = mock(ChannelFuture.class);
    ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);

    // when
    when(ctx.writeAndFlush(eq(EXPECTED))).thenReturn(future);
    handler.eventReceived(ctx, event);

    // then
    verify(ctx, times(0)).writeAndFlush(any());
    verify(future, times(0)).addListener(any());
  }

  private static IdleStateEvent[] use() {
    return new IdleStateEvent[]{IdleStateEvent.ALL_IDLE_STATE_EVENT, IdleStateEvent.WRITER_IDLE_STATE_EVENT,
      IdleStateEvent.READER_IDLE_STATE_EVENT};
  }

  private static IdleStateEvent[] notUse() {
    return new IdleStateEvent[]{IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT, IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT,
      IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT};
  }
}