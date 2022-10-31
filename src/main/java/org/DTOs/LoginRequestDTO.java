package org.DTOs;

public class LoginRequestDTO {
  public String username;
  public String password;
  boolean rememberMe;

  public LoginRequestDTO(String username, String password, boolean rememberMe) {
    this.username = username;
    this.password = password;
    this.rememberMe = rememberMe;
  }
}
