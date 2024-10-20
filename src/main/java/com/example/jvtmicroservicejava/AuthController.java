package com.example.jvtmicroservicejava;

import com.example.jvtmicroservicejava.dto.UserRegistrationDto;
import com.example.jvtmicroservicejava.entity.RefreshToken;
import com.example.jvtmicroservicejava.payloads.AuthRequest;
import com.example.jvtmicroservicejava.payloads.AuthResponse;
import com.example.jvtmicroservicejava.payloads.RefreshTokenRequest;
import com.example.jvtmicroservicejava.servise.AuthenticationService;
import com.example.jvtmicroservicejava.servise.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationService authenticationService;
  private final UserService userService;
  private final JwtUtil jwtUtil;

  @PostMapping("/login")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
    AuthResponse authResponse = authenticationService.authenticate(authRequest.getUsername(), authRequest.getPassword());
    return ResponseEntity.ok(authResponse);
  }

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto registrationDto) {
    try {
      userService.registerNewUser(registrationDto.getUsername(), registrationDto.getPassword());
      return ResponseEntity.ok("User registered successfully");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    RefreshToken refreshToken = authenticationService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
    String newJwt = jwtUtil.generateToken(refreshToken.getUser().getUsername());
    return ResponseEntity.ok(new AuthResponse(newJwt, refreshToken.getToken()));
  }
}