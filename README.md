[![Release](https://github.com/ppzxc/codec/actions/workflows/release.yml/badge.svg)](https://github.com/ppzxc/codec/actions/workflows/release.yml) [![Coverage](.github/badges/jacoco.svg)](https://github.com/ppzxc/codec/actions/workflows/main.yml)

# netty tcp codecs

- [netty](https://github.com/netty/netty) tcp codecs

# codec flow, before handshake

```text
            "Byte Array Stream"                     "Byte Array Stream"
                    |                                      /|\
                    |                                       |
                   \|/                                      |
FixedConstructorLengthFieldBasedFrameDecoder                |
                    |                                       |
                    |                                       |
                   \|/                                      | 
                    -----------------------------------------
                    |                                       |
                    |  HandShakeSimpleChannelInboundHandler |
                    |                                       |
                    ----------------------------------------     
```

# codec inbound flow, after handshake

```text
            "Byte Array Stream"                            "Byte Array Stream"
                    |                                             /|\
                    |                                              |
                   \|/                                             |
FixedConstructorLengthFieldBasedFrameDecoder                       |
                    |                                              |
                    |                                              |
                   \|/                                             |
      EncryptedInboundMessageDecoder                     OutboundMessageEncoder
                    |                                              |
                    |                                              |
                   \|/                                             |
             "InboundMessage"                                      |
   ( require next decoder or handler )                      "InboundMessage"
                    |                                             /|\
                    |                                              |
                   \|/                                             |
                    ------------------------------------------------
                    |                                              |
                    |         Some handler will handle it          |
                    |                                              |
                    ------------------------------------------------
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

1. HandshakeType

- required 'RSA 1024', other failure

| name     | hex  |
|----------|------|
| NONE     | 0x00 |
| RSA 1024 | 0x01 |
| RSA 2048 | 0x02 |
| RSA 4096 | 0x03 |

2. Type

- encryption type
- require 'AES', other failure

| name | hex  |
|------|------|
| NONE | 0x00 |
| AES  | 0x01 |

2. Mode

- encryption mode
- require 'CBC', other failure

| name | hex  |
|------|------|
| NONE | 0x00 |
| ECB  | 0x01 |
| CBC  | 0x02 |
| CFB  | 0x03 |
| OFB  | 0x04 |
| CTR  | 0x05 |

3. Padding

- encryption Padding
- require 'PKCS 7 PADDING', other failure

| name   | hex  |
|--------|------|
| NONE   | 0x00 |
| PKCS#5 | 0x01 |
| PKCS#7 | 0x02 |

4. IV Parameter

- require 16 byte fixed length.

5. Symmetric Key

- 16 or 24 or 32 byte variable length.

```text
NONE 
```

### before encrypt

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                           Length                              |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 | HandShakeType |      Type     |     Mode      |    Padding    |
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

| type   | name           | length              | binary                        | range                          | hexadecimal             |
|--------|----------------|---------------------|-------------------------------|--------------------------------|-------------------------|
| Header | Length         | 4 byte              | 32 bit                        | -2,147,483,648 ~ 2,147,483,647 | 0x80000000 ~ 0x7fffffff |  
| Header | HandShake Type | 1 byte              | 8 bit                         | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Header | Type           | 1 byte              | 8 bit                         | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Header | Mode           | 1 byte              | 8 bit                         | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Header | Padding        | 1 byte              | 8 bit                         | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Body   | IV Parameter   | 16 byte             | 128 bit                       | -                              | -                       |
| Body   | Symmetric Key  | 16 or 24 or 32 byte | 128 bit or 192 bit or 256 bit | -                              | -                       |

- iv parameter rule

```text
set 'default' if null padding. 
'default' value is server private.
```

### after encrypt

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

| type   | name           | length   | binary   | range                          | hexadecimal             |
|--------|----------------|----------|----------|--------------------------------|-------------------------|
| Header | Length         | 4 byte   | 32 bit   | -2,147,483,648 ~ 2,147,483,647 | 0x80000000 ~ 0x7fffffff |  
| Header | HandShake Type | 1 byte   | 8 bit    | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Header | Type           | 1 byte   | 8 bit    | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Header | Mode           | 1 byte   | 8 bit    | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Header | Padding        | 1 byte   | 8 bit    | -128 ~ 127                     | 0x80 ~ 0x7f             |
| Body   | Encrypted Body | variable | variable | variable                       | variable                |

- encrypted body rule

```text
1. 'handshake' uses 'rsa' algorithm.
2. Public key algorithm for 'AES' key exchange.
3. The 'AES' key is provided by the client.
```

## message

### before encrypt

```text
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                             Length                            |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                               ID                              |
 |                                                               |                            
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |     Type    |    Status     |   Encoding    |    Reserved     |
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 |                              ...                              |
 |                              Body                             |
 |                              ...                              |                            
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  0                   1                   2                   3
  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
```

| type   | name     | length   | binary   | range                                                  | hexadecimal                             |
|--------|----------|----------|----------|--------------------------------------------------------|-----------------------------------------|
| Header | Length   | 4 byte   | 32 bit   | -2,147,483,648 ~ 2,147,483,647                         | 0x80000000 ~ 0x7fffffff                 |
| Header | ID       | 8 byte   | 64 bit   | -9,223,372,036,854,775,808 ~ 9,223,372,036,854,775,807 | 0x8000000000000000 ~ 0x7fffffffffffffff |
| Header | Type     | 1 byte   | 8 bit    | -128 ~ 127                                             | 0x80 ~ 0x7f                             |
| Header | Status   | 1 byte   | 8 bit    | -128 ~ 127                                             | 0x80 ~ 0x7f                             |
| Header | Encoding | 1 byte   | 8 bit    | -128 ~ 127                                             | 0x80 ~ 0x7f                             |
| Header | Reserved | 1 byte   | 8 bit    | -128 ~ 127                                             | 0x80 ~ 0x7f                             |
| Body   | Body     | variable | variable | -                                                      | -                                       |

### after encrypt

- encryption fields

```text
Id, Type, Status, Encoding, Reserved, Body
```

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
