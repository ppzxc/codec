package io.github.ppzxc.codec.model;

import io.github.ppzxc.fixh.BooleanUtils;
import io.github.ppzxc.fixh.IntUtils;
import io.github.ppzxc.fixh.StringUtils;
import java.io.Serializable;

public class TestUser implements Serializable {

  private static final long serialVersionUID = -8962487339705879461L;
  private String username;
  private String password;
  private int age;
  private boolean payed;

  public TestUser() {
  }

  public TestUser(String username, String password, int age, boolean payed) {
    this.username = username;
    this.password = password;
    this.age = age;
    this.payed = payed;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public boolean isPayed() {
    return payed;
  }

  public void setPayed(boolean payed) {
    this.payed = payed;
  }

  public static TestUser random() {
    return new TestUser(StringUtils.giveMeOne(100), StringUtils.giveMeOne(100),
      IntUtils.giveMeOne(), BooleanUtils.giveMeOne());
  }
}
