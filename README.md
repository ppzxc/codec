[![Release](https://github.com/ppzxc/codec/actions/workflows/release.yml/badge.svg)](https://github.com/ppzxc/codec/actions/workflows/release.yml)

# netty tcp codecs

- [netty](https://github.com/netty/netty) tcp codecs

# codec flow

```text
                               ByteBufDecoder
                                     |
                                    \|/
                            RawInboundPacketDecoder
                             /                  \
                            /                    \
                          \|/                    \|/
EncryptedHandShakePacketDecoder                 Next Decoder In PipeLine
```

# packet structure

## binary

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                               Id                              |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |      Type     |     Status    |    Encoding   |    Reserved   |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                             Length                            |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
```

## binary description

| name        | length | binary | decimal                        | hexadecimal             | unit         |
|-------------|--------|--------|--------------------------------|-------------------------|--------------|
| Id          | 4 byte | 32 bit | -2,147,483,648 ~ 2,147,483,647 | 0x00000000 ~ 0xffffffff | Signed int32 |
| Type        | 1 byte | 8 bit  | -128 ~ 127                     | 0x00 ~ 0xff             | Signed int8  |
| Status      | 1 byte | 8 bit  | -128 ~ 127                     | 0x00 ~ 0xff             | Signed int8  |
| Encoding    | 1 byte | 8 bit  | -128 ~ 127                     | 0x00 ~ 0xff             | Signed int8  |
| Reserved    | 1 byte | 8 bit  | -128 ~ 127                     | 0x00 ~ 0xff             | Signed int8  |
| Body Length | 4 byte | 32 bit | -2,147,483,648 ~ 2,147,483,647 | 0x00000000 ~ 0xffffffff | Signed int32 |

## other rule

### handshake rule

```text
'handshake' uses 'rsa'.
```

- Public key algorithm for 'AES' key exchange.
- The 'AES' key is provided by the client.

### after handshake rule

```text
After 'handshake', the body of all packets communicates using 'AES'.
```

### body end rule

```text
The end of the body should always end with 'CrLf'.
```

- Server can verify that the Body Length is normal.
- On the server side, the 'body length Delimiter' method and the 'CrLf Delimiter' method can be used interchangeably.

### overflow Id rule

```text
If 'ID' is out of the range of signed int32, it is set to the initial value.
```