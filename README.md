[![Release](https://github.com/ppzxc/codec/actions/workflows/release.yml/badge.svg)](https://github.com/ppzxc/codec/actions/workflows/release.yml) [![Coverage](.github/badges/jacoco.svg)](https://github.com/ppzxc/codec/actions/workflows/main.yml)

# netty tcp codecs

- [netty](https://github.com/netty/netty) tcp codecs

# codec inbound flow

```text
            "Byte Array Stream"
                    |
                    |
                   \|/
FixedConstructorLengthFieldBasedFrameDecoder
                    |
                    |
                   \|/
           SecureChannelHandler
                    |
                    |
                   \|/
              ByteBufDecoder
                    |
                    |
                   \|/
             "InboundMessage"
   ( require next decoder or handler )
```

# codec outbound flow

```text
   "Byte Array Stream"
          /|\
           |
           |
 OutboundMessageEncoder
          /|\
           |
           |
    "OutboundMessage"
```

# message structure

## rule

### body end rule

```text
The end of the body should always end with 'CrLf'.
```

- Server can verify that the Body Length is normal.
- On the server side, the 'body length Delimiter' method and the 'CrLf Delimiter' method can be used interchangeably.

## common structure

### header

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                           Length                              |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
```

| name   | length | binary | range                          | hexadecimal             |
|--------|--------|--------|--------------------------------|-------------------------|
| Length | 4 byte | 32 bit | -2,147,483,648 ~ 2,147,483,647 | 0x00000000 ~ 0xffffffff |

### body

- body is [handShake](#handshake) or [message](#message)

## handShake

### header

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 | HandShakeType |  EncryptType  |  EncryptMode  | EncryptPadding|
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
```

| name            | length | binary | range      | hexadecimal |
|-----------------|--------|--------|------------|-------------|
| HandShake Type  | 1 byte | 8 bit  | -128 ~ 127 | 0x00 ~ 0xff |
| Encrypt Type    | 1 byte | 8 bit  | -128 ~ 127 | 0x00 ~ 0xff |
| Encrypt Mode    | 1 byte | 8 bit  | -128 ~ 127 | 0x00 ~ 0xff |
| Encrypt Padding | 1 byte | 8 bit  | -128 ~ 127 | 0x00 ~ 0xff |

### body

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                              ...                              |
 |                          IV Parameter                         |
 |                              ...                              |
 |                              ...                              |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                              ...                              |
 |                              ...                              |
 |                              ...                              |
 |                          Symmetric Key                        |
 |                              ...                              |
 |                              ...                              |
 |                              ...                              |
 |                              ...                              |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
```

| name          | length              | description                           |
|---------------|---------------------|---------------------------------------|
| IV Parameter  | 16 byte             | fixed length                          |
| Symmetric Key | 16 or 24 or 32 byte | like aes key length 128, 192, 256 bit |

### iv parameter rule

```text
set 'default' if null padding. 
'default' value is server private.
```

### full

- encrypt [body](#body) using [RSA](#encrypt-handshake-rule)

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                           Length                              |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 | HandShakeType |  EncryptType  |  EncryptMode  | EncryptPadding|
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                              ...                              |
 |                              ...                              |
 |                        Encrypted Body                         |
 |                              ...                              |
 |                              ...                              |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
```

| name           | length   | binary   | range                          | hexadecimal             | type           |
|----------------|----------|----------|--------------------------------|-------------------------|----------------|
| Type           | 1 byte   | 8 bit    | -128 ~ 127                     | 0x00 ~ 0xff             | Signed Integer |
| Mode           | 1 byte   | 8 bit    | -128 ~ 127                     | 0x00 ~ 0xff             | Signed Integer |
| Padding        | 1 byte   | 8 bit    | -128 ~ 127                     | 0x00 ~ 0xff             | Signed Integer |
| Body Length    | 4 byte   | 32 bit   | -2,147,483,648 ~ 2,147,483,647 | 0x00000000 ~ 0xffffffff | Signed Integer |
| Encrypted Body | variable | variable | variable                       | variable                | variable       |

### encrypted body rule

```text
1. 'handshake' uses 'rsa' algorithm.
```

- Public key algorithm for 'AES' key exchange.
- The 'AES' key is provided by the client.

## message

### after handshake rule

```text
After 'handshake', the body of all messages communicates using 'AES'.
```

### overflow Id rule

```text
If 'ID' is out of the range of signed int32, it is set to the initial value.
```

# usage

- [sonatype](https://central.sonatype.com/artifact/io.github.ppzxc/codec)

```
implementation("io.github.ppzxc:codec:X.X.X")
```

### encrypt handshake rule

```text
'handshake' uses 'rsa'.
```

- Public key algorithm for 'AES' key exchange.
- The 'AES' key is provided by the client.