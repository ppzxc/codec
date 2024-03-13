package io.github.ppzxc.codec.model;

import java.io.Serializable;

public interface Message extends Serializable {

  Header header();
}
