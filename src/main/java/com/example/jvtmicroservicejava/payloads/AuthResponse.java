package com.example.jvtmicroservicejava.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
  private final String jwt;
  private final String refreshToken;
}