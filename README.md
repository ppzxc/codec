[![Release](https://github.com/ppzxc/codec/actions/workflows/release.yml/badge.svg)](https://github.com/ppzxc/codec/actions/workflows/release.yml)

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
                               ByteBufDecoder
                               /            \
                              /              \
                            \|/              \|/
EncryptedHandShakeMessageDecoder            "InboundMessage" ( require next decoder )
               |                                       
               |
              \|/
       "HandShakeMessage"
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

# Protocol Buffer IDL

## EncryptionMethodProtobuf

- [./src/main/protobuf/encryption_method.proto](./src/main/protobuf/encryption_method.proto)

```protobuf
syntax = "proto3";

message EncryptionMethodProtobuf {
  EncryptionTypeProtobuf type = 1;
  EncryptionModeProtobuf mode = 2;
  EncryptionPaddingProtobuf padding = 3;
  string iv = 4;
  string symmetricKey = 5;
}

enum EncryptionTypeProtobuf {
  ADVANCED_ENCRYPTION_STANDARD = 0;
}

enum EncryptionModeProtobuf {
  ELECTRONIC_CODE_BLOCK = 0;
  CIPHER_BLOCK_CHAINING = 1;
  CIPHER_FEEDBACK = 2;
  OUTPUT_FEEDBACK = 3;
  COUNTER = 4;
}

enum EncryptionPaddingProtobuf {
  PKCS5PADDING = 0;
  PKCS7PADDING = 1;
}
```

# message structure

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
After 'handshake', the body of all messages communicates using 'AES'.
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