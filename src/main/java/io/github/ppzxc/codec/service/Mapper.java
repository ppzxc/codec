package io.github.ppzxc.codec.service;

public interface Mapper {

  <T> T read(byte[] payload, Class<T> tClass);

  <T> byte[] write(T object);
}
