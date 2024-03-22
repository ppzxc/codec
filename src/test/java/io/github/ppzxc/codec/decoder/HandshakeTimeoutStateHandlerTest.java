package io.github.ppzxc.codec.decoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.ppzxc.codec.model.CodecCode;
import io.github.ppzxc.codec.model.HandshakeResult;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class HandshakeTimeoutStateHandlerTest {

  private HandshakeTimeoutStateHandler handler;

  @BeforeEach
  void setUp() {
    handler = new HandshakeTimeoutStateHandler();
  }

  @ParameterizedTest
  @MethodSource("all")
  void should_no_behavior(IdleStateEvent event) throws Exception {
    // given
    ChannelFuture future = mock(ChannelFuture.class);
    ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);

    // when
    when(ctx.writeAndFlush(eq(HandshakeResult.of(CodecCode.HANDSHAKE_TIMEOUT_NO_BEHAVIOR)))).thenReturn(future);
    handler.eventReceived(ctx, event);

    // then
    verify(ctx, times(1)).writeAndFlush(any());
    verify(future, times(1)).addListener(any());
  }

  @ParameterizedTest
  @MethodSource("reader")
  void should_no_incoming(IdleStateEvent event) throws Exception {
    // given
    ChannelFuture future = mock(ChannelFuture.class);
    ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);

    // when
    when(ctx.writeAndFlush(eq(HandshakeResult.of(CodecCode.HANDSHAKE_TIMEOUT_NO_INCOMING)))).thenReturn(future);
    handler.eventReceived(ctx, event);

    // then
    verify(ctx, times(1)).writeAndFlush(any());
    verify(future, times(1)).addListener(any());
  }

  @ParameterizedTest
  @MethodSource("writer")
  void should_no_outgoing(IdleStateEvent event) throws Exception {
    // given
    ChannelFuture future = mock(ChannelFuture.class);
    ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);

    // when
    when(ctx.writeAndFlush(eq(HandshakeResult.of(CodecCode.HANDSHAKE_TIMEOUT_NO_OUTGOING)))).thenReturn(future);
    handler.eventReceived(ctx, event);

    // then
    verify(ctx, times(1)).writeAndFlush(any());
    verify(future, times(1)).addListener(any());
  }

  private static IdleStateEvent[] all() {
    return new IdleStateEvent[]{IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT, IdleStateEvent.ALL_IDLE_STATE_EVENT};
  }

  private static IdleStateEvent[] reader() {
    return new IdleStateEvent[]{IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT, IdleStateEvent.READER_IDLE_STATE_EVENT};
  }

  private static IdleStateEvent[] writer() {
    return new IdleStateEvent[]{IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT, IdleStateEvent.WRITER_IDLE_STATE_EVENT};
  }
}