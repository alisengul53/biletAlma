package org.DTOs;

public class LoginResponseDTO {
  public int totalItemCount;
  public Value value;
  public boolean isError;
  public int resultCode;

  public LoginResponseDTO(int totalItemCount, Value value, boolean isError, int resultCode) {
    this.totalItemCount = totalItemCount;
    this.value = value;
    this.isError = isError;
    this.resultCode = resultCode;
  }
  public LoginResponseDTO()
  {
  }
}
