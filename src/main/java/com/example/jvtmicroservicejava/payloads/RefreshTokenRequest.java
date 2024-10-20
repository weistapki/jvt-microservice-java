package com.example.jvtmicroservicejava.payloads;

import lombok.Data;

@Data
public class RefreshTokenRequest {
  private String refreshToken;
}

