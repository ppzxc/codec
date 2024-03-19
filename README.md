[![Release](https://github.com/ppzxc/codec/actions/workflows/release.yml/badge.svg)](https://github.com/ppzxc/codec/actions/workflows/release.yml) [![Coverage](.github/badges/jacoco.svg)](https://github.com/ppzxc/codec/actions/workflows/main.yml)

# netty tcp codecs

- [netty](https://github.com/netty/netty) tcp codecs

# codec inbound flow, before handshake

```text
            "Byte Array Stream"
                    |
                    |
                   \|/
FixedConstructorLengthFieldBasedFrameDecoder
                    |
                    |
                   \|/
    HandShakeSimpleChannelInboundHandler
```

# codec inbound flow, after handshake

```text
            "Byte Array Stream"
                    |
                    |
                   \|/
FixedConstructorLengthFieldBasedFrameDecoder
                    |
                    |
                   \|/
      EncryptedInboundMessageDecoder
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

## common rule

### body end rule

```text
The end of the body should always end with 'CrLf'.
```

- Server can verify that the Body Length is normal.
- On the server side, the 'body length Delimiter' method and the 'CrLf Delimiter' method can be used interchangeably.

## handShake

### header

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                           Length                              |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 | HandShakeType |      Type     |     Mode      |    Padding    |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
```

| name           | length | binary | range                          | hexadecimal             |
|----------------|--------|--------|--------------------------------|-------------------------|
| Length         | 4 byte | 32 bit | -2,147,483,648 ~ 2,147,483,647 | 0x80000000 ~ 0x7fffffff |  
| HandShake Type | 1 byte | 8 bit  | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Type           | 1 byte | 8 bit  | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Mode           | 1 byte | 8 bit  | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Padding        | 1 byte | 8 bit  | -128 ~ 127                     | 0x80 ~ 0x7f             |

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

| name           | length   | binary   | range                          | hexadecimal             |
|----------------|----------|----------|--------------------------------|-------------------------|
| Length         | 4 byte   | 32 bit   | -2,147,483,648 ~ 2,147,483,647 | 0x80000000 ~ 0x7fffffff |
| HandShakeType  | 1 byte   | 8 bit    | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Type           | 1 byte   | 8 bit    | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Mode           | 1 byte   | 8 bit    | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Padding        | 1 byte   | 8 bit    | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Encrypted Body | variable | variable | variable                       | variable                |

### encrypted body rule

```text
1. 'handshake' uses 'rsa' algorithm.
```

- Public key algorithm for 'AES' key exchange.
- The 'AES' key is provided by the client.

## message

### header

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                             Length                            |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                               ID                              |
 |                                                               |                            
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 | HandShakeType |      Type     |     Mode      |    Padding    |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
```

| name    | length | binary | range                                                  | hexadecimal                             |
|---------|--------|--------|--------------------------------------------------------|-----------------------------------------|
| Length  | 4 byte | 32 bit | -2,147,483,648 ~ 2,147,483,647                         | 0x80000000 ~ 0x7fffffff                 |
| ID      | 8 byte | 64 bit | -9,223,372,036,854,775,808 ~ 9,223,372,036,854,775,807 | 0x8000000000000000 ~ 0x7fffffffffffffff |
| Type    | 1 byte | 8 bit  | -128 ~ 127                                             | 0x80 ~ 0x7f                             |
| Mode    | 1 byte | 8 bit  | -128 ~ 127                                             | 0x80 ~ 0x7f                             |
| Padding | 1 byte | 8 bit  | -128 ~ 127                                             | 0x80 ~ 0x7f                             |

### body

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                              ...                              |
 |                              Body                             |
 |                              ...                              |                            
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
```

| name | length   | binary   | 
|------|----------|----------|
| Body | variable | variable |

### full

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                             Length                            |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                              ...                              |
 |                         Encrypted Body                        |
 |                              ...                              |                            
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
```

| name   | length   | binary   | range                          | hexadecimal             |
|--------|----------|----------|--------------------------------|-------------------------|
| Length | 4 byte   | 32 bit   | -2,147,483,648 ~ 2,147,483,647 | 0x80000000 ~ 0x7fffffff |
| Body   | variable | variable | variable                       | variable                |

- encrypted body rule

```text
Except for the length field, all headers and bodies are encrypted by 'AES'.
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