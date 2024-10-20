package com.example.jvtmicroservicejava.servise;

import com.example.jvtmicroservicejava.JwtUtil;
import com.example.jvtmicroservicejava.entity.RefreshToken;
import com.example.jvtmicroservicejava.exeption.InvalidCredentialsException;
import com.example.jvtmicroservicejava.exeption.InvalidRefreshTokenException;
import com.example.jvtmicroservicejava.repository.RefreshTokenRepository;
import com.example.jvtmicroservicejava.entity.User;
import com.example.jvtmicroservicejava.payloads.AuthResponse;
import com.example.jvtmicroservicejava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//  private final JwtUtil jwtUtil;
//  private final UserDetailsService userDetailsService;
//  private final UserRepository userRepository;  // Внедряем UserRepository
//  private final RefreshTokenRepository refreshTokenRepository;
//  private final AuthenticationManager authenticationManager;
//  private final PasswordEncoder passwordEncoder;  // Внедряем PasswordEncoder
//
//  public AuthResponse authenticate(String username, String password) throws Exception {
//    try {
//      // Аутентификация пользователя
//      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//    } catch (Exception e) {
//      throw new Exception("Invalid credentials");
//    }
//
//    // Загружаем пользователя
//    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//    // Извлекаем объект User из репозитория
//    User user = userRepository.findByUsername(username)
//        .orElseThrow(() -> new Exception("User not found"));
//
//    // Проверяем пароль через passwordEncoder
//    if (!passwordEncoder.matches(password, user.getPassword())) {
//      throw new Exception("Invalid credentials");
//    }
//
//    // Генерация JWT
//    String jwt = jwtUtil.generateToken(userDetails.getUsername());
//
//    // Генерация refresh токена
//    RefreshToken refreshToken = generateRefreshToken(user);
//
//    return new AuthResponse(jwt, refreshToken.getToken());
//  }
//
//  // Генерация refresh токена
//  public RefreshToken generateRefreshToken(User user) {
//    RefreshToken refreshToken = new RefreshToken();
//    refreshToken.setToken(UUID.randomUUID().toString());
//    refreshToken.setUser(user);
//    refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)); // 7 дней
//    return refreshTokenRepository.save(refreshToken);
//  }
//
//  // Валидация refresh токена
//  public RefreshToken validateRefreshToken(String token) {
//    return refreshTokenRepository.findByToken(token)
//        .orElseThrow(() -> new RuntimeException("Refresh token is invalid!"));
//  }
//}
@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;
  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  public AuthResponse authenticate(String username, String password) {
    // Попытка аутентификации пользователя
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (Exception e) {
      throw new InvalidCredentialsException("Invalid credentials"); // Бросаем кастомное исключение
    }

    // Получение пользователя
    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new InvalidCredentialsException("User not found"));

    // Проверка пароля
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new InvalidCredentialsException("Invalid credentials");
    }

    // Генерация JWT и refresh токена
    String jwt = jwtUtil.generateToken(userDetails.getUsername());
    RefreshToken refreshToken = generateRefreshToken(user);

    return new AuthResponse(jwt, refreshToken.getToken());
  }

  public RefreshToken generateRefreshToken(User user) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setUser(user);
    refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)); // 7 дней
    return refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken validateRefreshToken(String token) {
    return refreshTokenRepository.findByToken(token)
        .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token is invalid or expired"));
  }
}

