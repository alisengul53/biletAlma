package org.DTOs;

public class Value {
  public String createdOn;
  public boolean isExpired;
  public String expireDate;
  public String access_token;
  public String refresh_token;
  public String token_type;
  public long expires_in;

  public Value(String createdOn, boolean isExpired, String expireDate, String access_token, String refresh_token,
    String token_type, long expires_in) {
    this.createdOn = createdOn;
    this.isExpired = isExpired;
    this.expireDate = expireDate;
    this.access_token = access_token;
    this.refresh_token = refresh_token;
    this.token_type = token_type;
    this.expires_in = expires_in;
  }
  public Value() {
  }
}